package pinger.challenge.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.viewmodel.ext.android.viewModel
import pinger.challenge.R
import pinger.challenge.intent.PageSequenceIntent
import pinger.challenge.viewmodel.PageSequenceViewModel

class PageSequenceActivity : AppCompatActivity() {

    private val viewModel: PageSequenceViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PageSequenceScreen(viewModel)
        }
        viewModel.processIntent(PageSequenceIntent.FetchLogsIntent)
    }

    @Composable
    fun PageSequenceScreen(viewModel: PageSequenceViewModel) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Pinger Challenge") },
                    elevation = 4.dp,
                    backgroundColor = colorResource(id = R.color.colorPrimary),
                    contentColor = Color.White
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = colorResource(id = R.color.colorPrimary),
                    contentColor = Color.White,
                    onClick = {
                        viewModel.processIntent(PageSequenceIntent.FetchLogsIntent)
                    },
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    val icon = painterResource(id = R.drawable.ic_cloud_download_white_24dp)
                    Icon(icon, contentDescription = "Download")
                }
            },
            content = {
                it
                PageSequenceContent(viewModel)
            }
        )
    }

    @Composable
    fun PageSequenceContent(viewModel: PageSequenceViewModel) {
        val pageSequenceData by viewModel.pageSequenceData.observeAsState(emptyList())
        val loadingData by viewModel.loadingData.observeAsState(true)

        Box(Modifier.fillMaxSize()) {
            LazyColumn {
                items(pageSequenceData.size) { index ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                        elevation = 4.dp
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            val item = pageSequenceData[index]
                            val label = pluralStringResource(
                                id = R.plurals.repeated_string,
                                item.second,
                                item.second
                            )
                            Text(
                                color = Color.Gray,
                                text = label,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                            )
                            Text(
                                text = item.first,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 8.dp,
                                    bottom = 10.dp
                                )
                            )
                        }
                    }
                }
            }
            if (loadingData) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
