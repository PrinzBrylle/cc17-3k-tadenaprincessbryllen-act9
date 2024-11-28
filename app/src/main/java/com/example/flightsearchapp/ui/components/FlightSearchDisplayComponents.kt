package com.example.flightsearchapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flightsearchapp.R
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.ui.theme.CustomLightGray

@Composable
fun FlightSearchDisplay(
    airportDeparture: Airport,
    onClickAirport: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickAirport(airportDeparture.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = airportDeparture.iata,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = airportDeparture.name, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun FlightDestinationDetails(
    airportFrom: Airport,
    airportTo: Airport,
    onSaveFavorite: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = Color.Black,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CustomLightGray),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    FlightDetailSubTitle(R.string.flight_detail_departure)
                    FlightSearchDisplay(airportFrom)

                    Spacer(modifier = Modifier.height(8.dp))

                    FlightDetailSubTitle(R.string.flight_detail_arrive)
                    FlightSearchDisplay(airportTo)
                }

                IconButton(onClick = { onSaveFavorite() }) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(R.string.icon_save_favorite),
                        tint = buttonColor
                    )
                }
            }
        }
    }
}

@Composable
private fun FlightDetailSubTitle(@StringRes text: Int) {
    Text(
        text = stringResource(text),
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        fontSize = 12.sp
    )
}