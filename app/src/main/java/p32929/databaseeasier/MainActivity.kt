package p32929.databaseeasier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import p32929.androideasysql_library.EasyDB
import java.text.DateFormat
import java.util.Date

/** A small notes app that shows EasyDB doing add, read, update and delete. */
class MainActivity : ComponentActivity() {

    private val db by lazy {
        EasyDB.init(this, "Notes")
            .setTableName("Notes")
            .addColumn("Title", "text", "not null")
            .addColumn("Body", "text")
            .addColumn("Edited", "integer")
            .doneTableColumn()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (db.count() == 0L) seed()
        setContent {
            val system = isSystemInDarkTheme()
            var dark by rememberSaveable { mutableStateOf(system) }
            MaterialTheme(colorScheme = if (dark) DarkColors else LightColors) {
                NotesScreen(db, dark, onToggleTheme = { dark = !dark })
            }
        }
    }

    private fun seed() {
        val now = System.currentTimeMillis()
        listOf(
            "Groceries" to "Milk, eggs, rice and two lemons.",
            "Ideas" to "A widget that shows today's notes on the home screen.",
            "Call Mum" to "Sunday evening, after dinner.",
        ).forEachIndexed { i, (title, body) ->
            db.addData("Title", title).addData("Body", body).addData("Edited", now - i * 3_600_000L).doneDataAdding()
        }
    }
}

private val LightColors = lightColorScheme(
    primary = Color(0xFF00696E), onPrimary = Color.White,
    primaryContainer = Color(0xFF9CF0F5), onPrimaryContainer = Color(0xFF002022),
    background = Color(0xFFF4FBFB), surface = Color(0xFFF4FBFB),
    surfaceContainerHighest = Color(0xFFDDE4E4),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF80D4D9), onPrimary = Color(0xFF003739),
    primaryContainer = Color(0xFF004F53), onPrimaryContainer = Color(0xFF9CF0F5),
    background = Color(0xFF0E1415), surface = Color(0xFF0E1415),
    surfaceContainerHighest = Color(0xFF2F3637),
)

private data class Note(val id: Int, val title: String, val body: String, val edited: Long)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotesScreen(db: EasyDB, dark: Boolean, onToggleTheme: () -> Unit) {
    var version by remember { mutableStateOf(0) }
    val notes = remember(version) {
        // Column 3 is "Edited": newest first.
        db.getAllRows(3, false).map {
            Note((it["ID"] as Long).toInt(), it["Title"] as String, it["Body"] as String? ?: "", it["Edited"] as Long? ?: 0)
        }
    }
    var editing by remember { mutableStateOf<Note?>(null) }
    var creating by remember { mutableStateOf(false) }
    val scroll = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scroll.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Notes", fontWeight = FontWeight.SemiBold)
                        Text(
                            "${notes.size} saved with EasyDB",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(if (dark) Icons.Rounded.LightMode else Icons.Rounded.DarkMode, "Switch theme")
                    }
                },
                scrollBehavior = scroll,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { creating = true },
                icon = { Icon(Icons.Rounded.Add, null) },
                text = { Text("New note") },
            )
        },
    ) { padding ->
        if (notes.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Rounded.StickyNote2, null, Modifier.size(56.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(12.dp))
                    Text("No notes yet. Tap New note to add one.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = padding.calculateTopPadding(), bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note,
                        onClick = { editing = note },
                        onDelete = { db.deleteRow(note.id); version++ },
                    )
                }
            }
        }
    }

    if (creating) {
        NoteDialog(null, onDismiss = { creating = false }) { title, body ->
            db.addData("Title", title).addData("Body", body).addData("Edited", System.currentTimeMillis()).doneDataAdding()
            creating = false; version++
        }
    }
    editing?.let { note ->
        NoteDialog(note, onDismiss = { editing = null }) { title, body ->
            db.updateData("Title", title).updateData("Body", body).updateData("Edited", System.currentTimeMillis()).rowID(note.id)
            editing = null; version++
        }
    }
}

@Composable
private fun NoteCard(note: Note, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp, end = 4.dp), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                Text(note.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (note.body.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(note.body, style = MaterialTheme.typography.bodyMedium, maxLines = 3, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "ID ${note.id} · " + DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(note.edited)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(onClick = onDelete) { Icon(Icons.Rounded.DeleteOutline, "Delete") }
        }
    }
}

@Composable
private fun NoteDialog(note: Note?, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var title by rememberSaveable { mutableStateOf(note?.title ?: "") }
    var body by rememberSaveable { mutableStateOf(note?.body ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (note == null) "New note" else "Edit note") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") }, singleLine = true)
                OutlinedTextField(body, { body = it }, label = { Text("Note") }, minLines = 3)
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(title.trim(), body.trim()) }, enabled = title.isNotBlank()) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}
