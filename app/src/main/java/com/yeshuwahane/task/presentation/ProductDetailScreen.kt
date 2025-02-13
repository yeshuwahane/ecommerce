package com.yeshuwahane.task.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.yeshuwahane.task.data.model.Attribute
import com.yeshuwahane.task.data.model.Data
import com.yeshuwahane.task.data.model.WSModel
import com.yeshuwahane.task.utils.DataResource
import com.yeshuwahane.task.utils.cleanText
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductDetailScreen(modifier: Modifier) {
    val viewModel = hiltViewModel<ProductDetailViewModel>()
    val uiState by viewModel.productDetailsState.collectAsStateWithLifecycle()

    if (uiState.productDetails.isLoading()){
        ProductDetailShimmer()
    }else if (uiState.productDetails.isSuccess()){
        ProductDetailCompose(uiState)
    } else{
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text("Error, problem loading your product")
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailCompose( uiState: ProductDetailState) {
    val productDetails = uiState.productDetails.data?.data
    val attributes = productDetails?.configurable_option?.firstOrNull()?.attributes.orEmpty()

    var selectedColorIndex by remember { mutableStateOf(0) }
    val allImages = attributes.flatMap { it.images }
    val pagerState = rememberPagerState(pageCount = { allImages.size })
    val coroutineScope = rememberCoroutineScope()
    var productQuantity by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = { ProductDetailTopBar(productDetails?.name) }
    ) { paddingValues ->
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (content, bottomButtons) = createRefs()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .constrainAs(content) { top.linkTo(parent.top) }
            ) {
                ImageSlider(allImages, pagerState)
                ImageIndicators(pagerState)

                ProductDetailsSection(productDetails, attributes, selectedColorIndex)

                ColorOptions(attributes, selectedColorIndex) { index, targetIndex ->
                    selectedColorIndex = index
                    coroutineScope.launch { pagerState.animateScrollToPage(targetIndex) }
                }

                PaymentInfoCard()

                QuantitySelector(productQuantity) { productQuantity = it }

                ProductInfoSection(productDetails?.description)

                Spacer(modifier = Modifier.height(120.dp))
            }

            BottomButtons(
                modifier = Modifier.constrainAs(bottomButtons) {
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        val currentImage = allImages.getOrNull(pagerState.currentPage)
        val newColorIndex = attributes.indexOfFirst { currentImage in it.images }
        if (newColorIndex != -1 && newColorIndex != selectedColorIndex) {
            selectedColorIndex = newColorIndex
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailTopBar(productName: String?) {
    TopAppBar(
        title = {
            productName?.let {
                Text(text = it, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* TODO: Handle back */ }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
            }
        },
        actions = {
            Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", Modifier.padding(2.dp))
            Icon(Icons.Default.IosShare, contentDescription = "Share", Modifier.padding(2.dp))
            Icon(Icons.Default.ShoppingBag, contentDescription = "Shopping bag", Modifier.padding(2.dp))
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(images: List<String>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth().aspectRatio(1f)
    ) { page ->
        images.getOrNull(page)?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageIndicators(pagerState: PagerState) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { index ->
            val color = if (pagerState.currentPage == index) Color.Black else Color(0xffd5c177)
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .clip(CircleShape)
                    .background(color)
                    .size(10.dp)
            )
        }
    }
}

@Composable
fun ProductDetailsSection(
    productDetails: Data?,
    attributes: List<Attribute>,
    selectedColorIndex: Int
) {
    Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = productDetails?.brand_name.orEmpty(),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "${attributes.getOrNull(selectedColorIndex)?.price ?: "0.000"} KWD",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

    Text(
        text = productDetails?.name.orEmpty(),
        color = Color.Gray,
        fontSize = 15.sp,
        modifier = Modifier.padding(horizontal = 10.dp)
    )

    Text(
        text = "SKU: ${productDetails?.sku}",
        color = Color.LightGray,
        fontSize = 15.sp,
        modifier = Modifier.padding(10.dp)
    )
}

@Composable
fun ColorOptions(
    attributes: List<Attribute>,
    selectedColorIndex: Int,
    onColorSelected: (Int, Int) -> Unit
) {
    val allImages = attributes.flatMap { it.images }
    Text(
        text = "Color:",
        fontSize = 17.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 10.dp, top = 10.dp)
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(attributes) { color ->
            val index = attributes.indexOf(color)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (index == selectedColorIndex) Color.Black else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable {
                        val targetImage = color.images.firstOrNull()
                        val targetIndex = allImages.indexOf(targetImage)
                        onColorSelected(index, targetIndex)
                    }
            ) {
                AsyncImage(
                    model = color.swatch_url,
                    contentDescription = color.option_id,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun PaymentInfoCard() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.padding(10.dp).fillMaxWidth().height(60.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "or 4 interest-free payment",
                fontSize = 13.sp
            )
            Row {
                Text(
                    text = "0.88 KWD",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Learn More",
                    fontSize = 13.sp,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Text(
        text = "Quantity",
        fontSize = 17.sp,
        color = Color.Gray,
        modifier = Modifier.padding(10.dp)
    )

    Row(modifier = Modifier.padding(10.dp)) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray)
                .clickable { if (quantity > 0) onQuantityChange(quantity - 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "-", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .height(40.dp)
                .width(60.dp)
                .border(1.dp, Color.Gray)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "$quantity", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Black)
                .clickable { onQuantityChange(quantity + 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "+", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun ProductInfoSection(description: String?) {
    var isExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "PRODUCT INFORMATION", fontSize = 17.sp)
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "Toggle Info"
        )
    }
    AnimatedVisibility(visible = isExpanded) {
        description?.let {
            Text(
                text = cleanText(it),
                fontSize = 13.sp,
                color = Color.Gray,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}




@Composable
fun BottomButtons(modifier: Modifier) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .background(color = Color.White)
            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .border(color = Color.Black, width = 1.5.dp)
                .background(shape = RectangleShape, color = Color.Black)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color.Black),
            onClick = { /*TODO*/ }) {
            Text(
                text = "Add to bag",
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.padding(top = 10.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.5.dp, color = Color.Black)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(Color.White),
            onClick = { /*TODO*/ }) {
            Text(
                text = "Share",
                color = Color.Black
            )
        }

    }
}
