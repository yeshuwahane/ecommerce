package com.yeshuwahane.task.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(modifier: Modifier = Modifier) {

    val viewModel = hiltViewModel<ProductDetailViewModel>()

    val productDetailsStateFlow by viewModel.productDetailsState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { productDetailsStateFlow.data?.images?.size ?: 0 })
    var productQuantity by remember { mutableStateOf(0) }
    var isProductDetailExpanded by remember { mutableStateOf(false) }
    val productOption by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(productDetailsStateFlow.data?.name ?: "") }
    ) {paddingValues->


        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(paddingValues)) {
            ProductImageSlider(productDetailsStateFlow, pagerState)
            ProductInfo(productDetailsStateFlow, productOption)
            ColorOptions(productDetailsStateFlow)
            PaymentCard()
            QuantitySelector(productQuantity) { productQuantity = it }
            ProductDetails(productDetailsStateFlow, isProductDetailExpanded) { isProductDetailExpanded = it }
            Spacer(modifier = Modifier.height(70.dp))
        }
        BottomButtons(modifier = Modifier.fillMaxWidth())
    }
}


@Composable
fun TopBar(productName: String) {
    TopAppBar(
        title = { Text(text = productName, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center) },
        navigationIcon = {
            IconButton(onClick = { /* TODO */ }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back")
            }
        },
        actions = {
            listOf(Icons.Default.FavoriteBorder, Icons.Default.IosShare, Icons.Default.ShoppingBag).forEach {
                Icon(imageVector = it, contentDescription = null, modifier = Modifier.padding(2.dp))
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageSlider(productDetails: ProductDetailsState, pagerState: PagerState) {
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
        Box(modifier = Modifier.fillMaxWidth().height(450.dp), contentAlignment = Alignment.TopStart) {
            AsyncImage(
                model = productDetails.data?.images?.get(page), contentDescription = null,
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ProductInfo(productDetails: ProductDetailsState, productOption: Int) {
    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)) {
        Text(text = productDetails.data?.brand_name ?: "", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = "${productDetails.data?.configurable_option?.get(0)?.attributes?.get(productOption)?.price} KWD",
            fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = productDetails.data?.name ?: "", color = Color.Gray, fontSize = 15.sp)
        Text(text = "SKU: ${productDetails.data?.sku}", color = Color.LightGray, fontSize = 15.sp)
    }
}

@Composable
fun ColorOptions(productDetails: ProductDetailsState) {
    LazyRow(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.Center) {
        items(productDetails.data?.configurable_option?.get(0)?.attributes ?: listOf()) { color ->
            Box(modifier = Modifier.padding(10.dp).border(2.dp, Color.Black, CircleShape)) {
                AsyncImage(model = color.swatch_url, contentDescription = color.value,
                    modifier = Modifier.padding(3.dp).clip(CircleShape).size(50.dp))
            }
        }
    }
}

@Composable
fun PaymentCard() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier.padding(10.dp).fillMaxWidth().height(70.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "or 4 interest-free payments", fontSize = 13.sp, textAlign = TextAlign.Start)
            Row {
                Text(text = "0.88 KWD  ", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "Learn More", fontSize = 13.sp, textDecoration = TextDecoration.Underline)
            }
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.clickable { if (quantity > 0) onQuantityChange(quantity - 1) }) { Text("-") }
        Text(text = "$quantity", modifier = Modifier.padding(horizontal = 10.dp))
        Box(modifier = Modifier.clickable { onQuantityChange(quantity + 1) }) { Text("+") }
    }
}

@Composable
fun ProductDetails(productDetails: ProductbDetailsState, expanded: Boolean, onExpandChange: (Boolean) -> Unit) {
    Column(modifier = Modifier.padding(15.dp)) {
        Row(modifier = Modifier.clickable { onExpandChange(!expanded) }.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "PRODUCT INFORMATION", fontSize = 17.sp)
            Icon(imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null)
        }
        AnimatedVisibility(visible = expanded, enter = expandVertically(), exit = shrinkVertically()) {
            Text(text = productDetails.data?.description ?: "", fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun BottomButtons(modifier: Modifier) {
    Column(modifier = modifier.padding(10.dp).background(Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(modifier = Modifier.fillMaxWidth().height(50.dp), onClick = { /*TODO*/ }) {
            Text(text = "Add to bag", color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(modifier = Modifier.fillMaxWidth().height(50.dp), onClick = { /*TODO*/ }) {
            Text(text = "Share", color = Color.Black)
        }
    }
}
