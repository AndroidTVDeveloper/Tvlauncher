package com.yang.tvlauncher.request;

import com.yang.tvlauncher.utils.NetUitls;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by
 * yangshuang on 2018/11/28.
 */

public class TencentRequest extends BaseRequest {
    private static ArrayList<HashMap<String, String>> data;

    public void request(final ResponseListener listener, boolean refresh) {
        if ((data == null || data.size() == 0) || refresh) {
            NetUitls.getHtmlString("https://v.qq.com/", new NetUitls.ReqeustListener() {
                @Override
                public void onResponse(String htmlString) {
                    if (htmlString != null && !htmlString.equals("")) {
                        Document document = Jsoup.parse(htmlString);
                        Elements elements = document.body().getElementsByClass("slider_nav");
                        Elements childs = elements.get(0).children();
                        if (childs.size() > 0) {
                            ArrayList<HashMap<String, String>> data = new ArrayList<>();
                            for (Element element : childs) {
                                HashMap<String, String> map = new HashMap<>();
                                String imageUrl = element.attr("data-bgimage");
                                Elements nameNode = element.getElementsByClass("tit");
                                Element element1 = nameNode.first();
                                map.put("name", element1.text());
                                map.put("image", "http:" + imageUrl);
                                data.add(map);
                            }
                            listener.onResponse(data);
                        } else {
                            listener.onResponse(null);
                        }
                    } else {
                        listener.onResponse(null);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    listener.onResponse(null);
                }
            });
        } else {
            listener.onResponse(data);
        }
    }
}