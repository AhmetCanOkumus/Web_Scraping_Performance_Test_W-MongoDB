package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.jsoup.select.Elements;

import java.io.IOException;

public class Vatan {
    public static void main(String[] args) {

        final String urlVatan = "https://www.vatanbilgisayar.com/cep-telefonu-modelleri/?page=" + 2;

        try {

            final Document document = Jsoup.connect(urlVatan).get();

            final Elements temelVatan = document.select("div.wrapper-product-main");

            for (Element xVatan: temelVatan.select("div.product-list.product-list--list-page")) {
                final String link = "https://www.vatanbilgisayar.com" +
                        xVatan.select("a.product-list__link").attr("href");
                final Document doc = Jsoup.connect(link).get();
                final String priceVatan = doc.select("div.d-cell.col-sm-3.col-xs-3.short-price span.product-list__price").text();
                final String nameVatan = doc.select("h1.product-list__product-name").text();
                System.out.println(nameVatan);
                System.out.println(priceVatan);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }




    }
}