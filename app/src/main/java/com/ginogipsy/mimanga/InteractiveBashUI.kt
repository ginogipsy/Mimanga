package com.ginogipsy.mimanga

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun InteractiveBashUI() {
    // Colore verde neon anni '80
    val neonGreen = Color(0xFF39FF14)

    // Stato per la cronologia dell'output del terminale
    var outputHistory by remember { mutableStateOf(listOf(":: GINOGIPSY OS v1.0 [RETRO MODE] ::", "Type 'help' for commands.")) }
    // Stato per il testo attualmente digitato dall'utente
    var inputText by remember { mutableStateOf("") }

    // Oggetti per gestire lo scorrimento automatico e il focus sul campo input
    val listState = rememberLazyListState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    // Effettua il focus sull'input all'avvio
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Layout principale: una colonna con output che scorre e un input fisso in basso
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .border(1.dp, neonGreen)
            .padding(8.dp)
    ) {
        // Area di output scorrevole
        LazyColumn(
            modifier = Modifier.weight(1f), // Occupa tutto lo spazio disponibile eccetto l'input
            state = listState,
            reverseLayout = false // L'output scorre verso l'alto
        ) {
            items(outputHistory) { line ->
                Text(
                    text = line,
                    color = neonGreen,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp
                )
            }
        }

        // Input fisso in basso
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "> ",
                color = neonGreen,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )

            // Campo di testo gestito
            BasicTextField(
                value = inputText,
                onValueChange = { inputText = it },
                textStyle = LocalTextStyle.current.copy(color = neonGreen, fontFamily = FontFamily.Monospace, fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    // Gestione del tasto Invio (Enter)
                    .onKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
                            val command = inputText.trim()
                            if (command.isNotEmpty()) {
                                // Aggiunge il comando alla cronologia (logica di processing)
                                outputHistory = outputHistory + "> $command"
                                // Logica di risposta (simulata)
                                outputHistory = outputHistory + "COMMAND '$command' EXECUTED (SIMULATION: TODO ADD LOGIC)"
                                inputText = "" // Resetta il campo di input

                                // Scorri automaticamente alla fine
                                coroutineScope.launch {
                                    listState.animateScrollToItem(outputHistory.lastIndex)
                                }
                            }
                            true
                        } else {
                            false
                        }
                    }
            )
        }
    }
}
