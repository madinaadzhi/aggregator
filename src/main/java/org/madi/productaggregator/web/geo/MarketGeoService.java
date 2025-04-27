package org.madi.productaggregator.web.geo;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.json.JSONObject;
import org.madi.productaggregator.web.dao.MarketRepository;
import org.madi.productaggregator.web.entities.MarketEntity;
import org.madi.productaggregator.web.geo.json.JsonReader;
import org.madi.productaggregator.web.market.api.Market;
import org.madi.productaggregator.web.market.api.MarketApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class MarketGeoService implements MarketFinder {
    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    @Qualifier("ForaMarketApi")
    private MarketApi foraMarketApi;

    @Autowired
    @Qualifier("SilpoMarketApi")
    private MarketApi silpoMarketApi;

    @Override
    public List<MarketDist> findMarkets(double lat, double lng) throws IOException {
        List<MarketDist> foundMarkets = new ArrayList<>();

        Iterable<MarketEntity> entities = marketRepository.findAll();
        List<Market> markets = new ArrayList<>();
        for (MarketEntity entity : entities) {
            if (entity.getDomainName().equals(foraMarketApi.getMarketInfo().getDomainName())) {
                Market fora = foraMarketApi.getMarketInfo();
                markets.add(fora);
            } else if (entity.getDomainName().equals(silpoMarketApi.getMarketInfo().getDomainName())) {
                Market silpo = silpoMarketApi.getMarketInfo();
                markets.add(silpo);
            }
        }
        for (Market market : markets) {
            int dist = getDistance(lat, lng, market.getLatitude(), market.getLongitude());
            foundMarkets.add(new MarketDist(market, dist));
        }

        foundMarkets.sort(Comparator.comparingInt(MarketDist::getDistance));
        return foundMarkets;
    }

    private int getDistance(double userLat, double userLng, double marketLat, double marketLng) throws IOException {
        String baseUrl = "https://maps.googleapis.com/maps/api/directions/json";
        Map<String, String> params = new HashMap<>();
        params.put("sensor", "false");
        params.put("mode", "walking");
        params.put("origin", userLat + "," + userLng);
        params.put("destination", marketLat + "," + marketLng);

        String url = baseUrl + '?' + encodeParams(params) + "&key=AIzaSyAYlgHTgIhp9laluhJ621gxZc5jnQtRIYk";

        JSONObject response = JsonReader.read(url);
        JSONObject location = response.getJSONArray("routes").getJSONObject(0);
        location = location.getJSONArray("legs").getJSONObject(0);
        int dist = location.getJSONObject("distance").getInt("value");
        return dist;
    }

    private static String encodeParams(Map<String, String> params) {
        String paramsUrl = Joiner.on('&').join(
                Iterables.transform(params.entrySet(), input -> {
                    try {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(input.getKey());
                        buffer.append('=');
                        buffer.append(URLEncoder.encode(input.getValue(), "utf-8"));
                        return buffer.toString();
                    } catch (final UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }));
        return paramsUrl;
    }
}
