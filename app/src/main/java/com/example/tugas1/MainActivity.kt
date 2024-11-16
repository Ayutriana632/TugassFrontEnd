package com.example.tugas1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tugas1.ui.theme.Tugas1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tugas1Theme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { MyTopAppBar(navController) },
                    bottomBar = { MyBottomNavigation(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Navigation(navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    TopAppBar(
        title = {
            Text(
                text = when (currentRoute) {
                    "home" -> "Petani & Produk Petani"
                    "crops" -> "Produk Petani"
                    "about" -> "About"
                    "detail/{itemName}" -> "Detail Informasi"
                    else -> "Aplikasi Petani"
                },
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            // Menampilkan tombol kembali hanya pada halaman detail
            if (currentRoute?.startsWith("detail") == true) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            // Tidak ada tombol menu atau aksi di sini
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF000000)) // Warna hitam
    )
}


@Composable
fun TriangleIcon(modifier: Modifier = Modifier, color: Color = Color.Black) {
    Canvas(modifier = modifier.size(24.dp)) {
        // Gunakan DrawScope.size untuk mendapatkan ukuran canvas
        val trianglePath = Path().apply {
            moveTo(size.width / 2f, 0f) // Titik atas
            lineTo(size.width, size.height) // Titik kanan bawah
            lineTo(0f, size.height) // Titik kiri bawah
            close() // Menutup path menjadi segitiga
        }

        drawPath(
            path = trianglePath,
            color = color
        )
    }
}

@Composable
fun MyBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Crops,
        BottomNavItem.About
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach {
            NavigationBarItem(
                icon = { TriangleIcon(color = if (currentRoute == it.screen_route) Color.Blue else Color.Black) }, // Segitiga dengan warna dinamis
                label = { Text(text = it.title, fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                selected = currentRoute == it.screen_route,
                onClick = {
                    navController.navigate(it.screen_route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}


sealed class BottomNavItem(var title: String, var screen_route: String, var icon: Int) {
    object Home : BottomNavItem("Home", "home", R.drawable.ic_launcher_foreground)
    object Crops : BottomNavItem("Crops", "crops", R.drawable.ic_launcher_foreground)
    object About : BottomNavItem("About", "about", R.drawable.ic_launcher_foreground)
}


@Composable
fun HomeScreen(navController: NavHostController) {
    AgricultureContent(
        navController = navController,
        crops = listOf("asep", "yamin", "lisa", "nisa", "adin", "dimas", "hamid", "ilyas", "maman", "udin"),
        tools = listOf("strawberry", "jeruk", "apel", "pepaya", "semangka", "rambutan", "brokoli", "terong", "tomat", "wortel")
    )
}

@Composable
fun CropsScreen(navController: NavHostController) {
    CropGrid(
        navController = navController,
        crops = listOf("strawberry", "jeruk", "apel", "pepaya", "semangka", "rambutan", "brokoli", "terong", "tomat", "wortel")
    )
}

@Composable
fun AboutScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = Color.Black
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile Image
            Image(
                painter = painterResource(id = R.drawable.profil),
                contentDescription = "Profil",
                modifier = Modifier
                    .height(300.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Details
            Text(
                text = "Profil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Ayu Triana\n" +
                        "ayutriana632@gmail.com\n" +
                        "Politeknik Negeri Sriwijaya\n" +
                        "Teknik Komputer",
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("crops") { CropsScreen(navController) }
        composable("about") { AboutScreen() }
        composable("detail/{itemName}") { backStackEntry ->
            val itemName = backStackEntry.arguments?.getString("itemName")
            DetailScreen(itemName = itemName ?: "Unknown")
        }
    }
}

@Composable
fun DetailScreen(itemName: String) {
    val descriptionMap = mapOf(
        "asep" to "Seorang petani strawberry.",
        "yamin" to "Seorang petani jeruk.",
        "lisa" to "Seorang petani apel.",
        "nisa" to "Seorang petani pepaya.",
        "adin" to "Seorang petani semangka.",
        "dimas" to "Seorang petani rambutan.",
        "hamid" to "Seorang petani brokoli.",
        "ilyas" to "Seorang petani terong.",
        "maman" to "Seorang petani tomat.",
        "udin" to "Seorang petani wortel.",
        "strawberry" to "Buah kecil berwarna merah cerah dengan biji kecil di permukaan. Rasanya manis sedikit asam dan sering digunakan dalam dessert.",
        "jeruk" to "Buah berkulit tebal berwarna oranye dengan daging yang segar dan kaya vitamin C. Rasanya manis hingga asam segar.",
        "apel" to "Buah berbentuk bulat dengan kulit berwarna merah, hijau, atau kuning. Rasanya renyah dan manis, terkadang asam.",
        "pepaya" to "Buah lonjong dengan kulit hijau kekuningan dan daging berwarna oranye. Rasanya manis lembut, kaya serat dan enzim pencernaan.",
        "semangka" to "Buah besar berkulit hijau dengan daging merah atau kuning. Rasanya manis dan sangat berair.",
        "rambutan" to "Buah tropis kecil dengan kulit berambut. Daging buahnya transparan, manis, dan sedikit kenyal.",
        "brokoli" to "Sayuran hijau berbentuk menyerupai pohon kecil. Kaya nutrisi dan sering dimasak dengan cara direbus, dikukus, atau ditumis.",
        "terong" to "Sayuran berbentuk lonjong dengan kulit ungu mengkilap. Dagingnya lembut dan sering digunakan dalam masakan seperti balado atau sup.",
        "tomat" to "Buah merah berbentuk bulat, sering dianggap sayur. Rasanya asam manis dan sering digunakan dalam salad, saus, atau sup.",
        "wortel" to " Sayuran berwarna oranye cerah dengan tekstur renyah. Rasanya manis alami dan kaya vitamin A.",
    )
    val imageRes = imageMap[itemName] ?: R.drawable.bg
    val description = descriptionMap[itemName] ?: "Deskripsi tidak tersedia."

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = itemName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = itemName,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(16.dp)
            )

            Text(
                text = "Deskripsi: $description",
                fontSize = 18.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun AgricultureContent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    crops: List<String>,
    tools: List<String>
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(25.dp)
    ) {
        Column(modifier.fillMaxSize()) {
            Text(text = "Petani", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
            LazyRow {
                items(items = crops) { crop ->
                    RowItem(modifier = modifier, name = crop, navController = navController)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Produk Petani",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )
            LazyColumn {
                items(items = tools) { tool ->
                    ColumnItem(modifier = modifier, name = tool, navController = navController)
                }
            }
        }
    }
}

@Composable
fun CropGrid(
    navController: NavHostController,
    crops: List<String>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(crops) { crop ->
            GridItem(name = crop, navController = navController)
        }
    }
}

@Composable
fun ToolGrid(
    navController: NavHostController,
    tools: List<String>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(tools) { tool ->
            GridItem(name = tool, navController = navController)
        }
    }
}

val imageMap = mapOf(
    "asep" to R.drawable.asep,
    "yamin" to R.drawable.yamin,
    "lisa" to R.drawable.lisa,
    "nisa" to R.drawable.nisa,
    "adin" to R.drawable.adin,
    "dimas" to R.drawable.dimas,
    "hamid" to R.drawable.hamid,
    "ilyas" to R.drawable.ilyas,
    "maman" to R.drawable.maman,
    "udin" to R.drawable.udin,
    "strawberry" to R.drawable.strawberry,
    "jeruk" to R.drawable.jeruk,
    "apel" to R.drawable.apel,
    "pepaya" to R.drawable.pepaya,
    "semangka" to R.drawable.semangka,
    "rambutan" to R.drawable.rambutan,
    "brokoli" to R.drawable.brokoli,
    "terong" to R.drawable.terong,
    "tomat" to R.drawable.tomat,
    "wortel" to R.drawable.wortel,
)

@Composable
fun GridItem(name: String, navController: NavHostController) {
    val imageRes = imageMap[name] ?: R.drawable.bg

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { navController.navigate("detail/$name") },
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Menampilkan gambar
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.3f)
                    .padding(10.dp)
            )
            // Menampilkan nama item di bawah gambar
            Text(
                text = name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ColumnItem(modifier: Modifier, name: String, navController: NavHostController) {
    val imageRes = imageMap[name] ?: R.drawable.bg
    Card(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .aspectRatio(6f)
            .clickable { navController.navigate("detail/$name") },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .height(35.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color.Transparent)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RowItem(modifier: Modifier, name: String, navController: NavHostController) {
    val imageRes = imageMap[name] ?: R.drawable.bg
    Card(
        modifier = modifier
            .padding(10.dp)
            .height(60.dp)
            .aspectRatio(1.5f)
            .clickable { navController.navigate("detail/$name") },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().padding(5.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@Composable
fun Pr() {
    val crops = listOf("asep", "yamin", "lisa", "nisa", "adin", "dimas", "hamid", "ilyas", "maman", "udin")
    val tools = listOf("strawberry", "jeruk", "apel", "pepaya", "semangka", "rambutan", "brokoli", "terong", "tomat", "wortel")
    val navController = rememberNavController()
    AgricultureContent(navController = navController, crops = crops, tools = tools)
}