package com.zfx.wanandroidcompose.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zfx.wanandroidcompose.R
import com.zfx.wanandroidcompose.data.Article

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier.fillMaxWidth(),
    data : Article,
    favoriteClick : () -> Unit,
    cardClick : () -> Unit
){

    Card(
        modifier = modifier
            .wrapContentHeight()
            .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp))
            .clickable { cardClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardColors(
            contentColor = colorResource(id = R.color.white),
            containerColor = colorResource(id = R.color.white),
            disabledContentColor = colorResource(id = R.color.white),
            disabledContainerColor = colorResource(id = R.color.white)
        )
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (data.type == 1){
                    Text(
                        modifier = Modifier
                            .background(
                                color = colorResource(id = R.color.color_FFD81E06).copy(alpha = 0.1f),  // 背景色（主题色，10% 透明度）
                                shape = RoundedCornerShape(4.dp)  // 圆角
                            )
                            .border(
                                width = 1.dp,  // 边框宽度
                                color = colorResource(id = R.color.color_FFD81E06),  // 边框颜色
                                shape = RoundedCornerShape(4.dp)  // 圆角（与背景一致）
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp),  // 内边距,
                        text = "置顶",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.color_FFD81E06)
                    )
                }else{
                    Image(
                        painter = painterResource(id = R.drawable.icon_article_logo),
                        contentDescription = "文章图标",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically),
                    text = if (data.author.isNotEmpty()){
                        "作者:${data.author}"
                    }else{
                        "分享人:${data.shareUser}"
                    },
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.black)
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = data.niceDate,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.nav_unselected)
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {


                Text(
                    modifier = Modifier.weight(1f),
                    text = data.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black)
                )
            }



            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    text = "${data.chapterName}/${data.superChapterName}",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.nav_selected)
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { favoriteClick() },
                    painter = painterResource(id = if(data.collect){
                        R.drawable.icon_heart_blue
                    }else{
                        R.drawable.icon_heart_grey
                    }),
                    contentDescription = "收藏图标"
                )
            }

        }
    }
}


