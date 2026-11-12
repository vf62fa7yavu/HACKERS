package com.hackers.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

private val Neon = Color(0xFF39FF14)
private val Bg = Color(0xFF070B09)
private val Panel = Color(0xFF101A13)
private val Muted = Color(0xFFA4B7A8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { HackersApp() }
    }
}

@Composable
fun HackersApp() {
    var age by remember { mutableStateOf("") }
    var system by remember { mutableStateOf("الثانوي العام") }
    var grade by remember { mutableStateOf("الأول الثانوي") }
    var track by remember { mutableStateOf("الطب وعلوم الحياة") }
    var subject by remember { mutableStateOf("الرياضيات") }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var screen by remember { mutableStateOf(0) }
    val clipboard = LocalClipboardManager.current

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Neon, background = Bg, surface = Panel,
        onPrimary = Color.Black, onBackground = Color.White, onSurface = Color.White
    )) {
        Surface(Modifier.fillMaxSize(), color = Bg) {
            Column(
                Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⌘", color = Neon, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(10.dp))
                    Text("HACKERS", color = Neon, fontSize = 27.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.weight(1f))
                    Text("AI STUDY", color = Neon, fontSize = 11.sp)
                }
                HorizontalDivider(color = Neon.copy(alpha = .35f))
                Text(
                    when (screen) {
                        0 -> "مساعدك الدراسي الذكي"
                        1 -> "بياناتك الدراسية"
                        else -> "حل الواجب"
                    },
                    color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold
                )
                Text("حل سريع • إجابة نهائية فقط", color = Muted)

                when (screen) {
                    0 -> {
                        CardBlock {
                            FieldLabel("عمرك كام سنة؟")
                            OutlinedTextField(age, { age = it }, modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("اكتب عمرك") }, singleLine = true)
                            FieldLabel("النظام الدراسي")
                            ChoiceRow(listOf("الثانوي العام", "البكالوريا المصرية", "الإعدادي", "الابتدائي"), system) { system = it }
                            FieldLabel("الصف الدراسي")
                            ChoiceRow(
                                if (system == "البكالوريا المصرية") listOf("الأول الثانوي", "الثاني الثانوي", "الثالث الثانوي")
                                else listOf("الأول الابتدائي","الثاني الابتدائي","الثالث الابتدائي","الرابع الابتدائي","الخامس الابتدائي","السادس الابتدائي","الأول الإعدادي","الثاني الإعدادي","الثالث الإعدادي","الأول الثانوي","الثاني الثانوي","الثالث الثانوي"),
                                grade
                            ) { grade = it }
                            if (system == "البكالوريا المصرية") {
                                FieldLabel("مسار البكالوريا")
                                ChoiceRow(listOf("الطب وعلوم الحياة", "الهندسة وعلوم الحاسب", "الأعمال", "الآداب والفنون"), track) { track = it }
                            }
                            Button(
                                onClick = { screen = 1 },
                                enabled = age.toIntOrNull() in 5..100,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Color.Black)
                            ) { Text("متابعة", fontWeight = FontWeight.Bold) }
                        }
                    }
                    1 -> {
                        CardBlock {
                            FieldLabel("اختار المادة")
                            ChoiceRow(listOf("الرياضيات", "العلوم", "اللغة العربية", "اللغة الإنجليزية", "الدراسات الاجتماعية", "الكمبيوتر", "أخرى"), subject) { subject = it }
                            Button(
                                onClick = { screen = 2 },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Color.Black)
                            ) { Text("ابدأ حل الواجب", fontWeight = FontWeight.Bold) }
                        }
                    }
                    else -> {
                        CardBlock {
                            FieldLabel("اكتب السؤال هنا")
                            OutlinedTextField(
                                question, { question = it }, modifier = Modifier.fillMaxWidth().heightIn(min = 130.dp),
                                placeholder = { Text("اكتب السؤال أو الصق نصه...") }
                            )
                            Text("المادة: $subject  •  الصف: $grade", color = Muted, fontSize = 12.sp)
                            Button(
                                onClick = { answer = "ميزة الذكاء الاصطناعي لم يتم ربطها بعد. سيتم إظهار الإجابة النهائية فقط هنا." },
                                enabled = question.isNotBlank(),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Neon, contentColor = Color.Black)
                            ) { Text("حل السؤال", fontWeight = FontWeight.Bold) }
                            if (answer.isNotBlank()) {
                                Text("الإجابة", color = Neon, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(answer, color = Color.White)
                                OutlinedButton(onClick = { clipboard.setText(AnnotatedString(answer)) }) {
                                    Text("نسخ الإجابة", color = Neon)
                                }
                            }
                        }
                    }
                }
                Text("HACKERS • LEARN SMARTER", color = Muted, fontSize = 10.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
private fun CardBlock(content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), content = content)
    }
}
@Composable
private fun FieldLabel(text: String) {
    Text(text, color = Neon, fontWeight = FontWeight.SemiBold)
}
@Composable
private fun ChoiceRow(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected == option, onClick = { onSelect(option) }, colors = RadioButtonDefaults.colors(selectedColor = Neon))
                Text(option, color = Color.White, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}
