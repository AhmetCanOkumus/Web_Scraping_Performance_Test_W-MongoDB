package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class TrendyolTest {

    public static void main(String[] args) throws InterruptedException {
        MemoryMXBean memoryMXBean=ManagementFactory.getMemoryMXBean();
        int threadCount = 3;
        Thread[] threads = new Thread[threadCount];
        double[] cpuUsages = new double[threadCount];
        double[] trendyolmemoryUsages = new double[threadCount];

        long startTime = System.currentTimeMillis();
        long startTime2 = System.nanoTime();










        for (int i = 0; i < threadCount; i++) {
            final int index = i;

            threads[i] = new Thread(() -> {
                for (int j = 2 + index; j < 5; j += threadCount) {
                    final String url = "https://www.trendyol.com/cep-telefonu-x-c103498?pi=" + j;

                    try {
                        final Document document = Jsoup.connect(url).get();
                        final Elements temel = document.select("div.prdct-cntnr-wrppr");

                        for (Element x : temel.select("div.p-card-wrppr.with-campaign-view")) {
                            final String link = "https://www.trendyol.com/" +
                                    x.select("div.p-card-chldrn-cntnr.card-border a").attr("href");
                            final Document doc = Jsoup.connect(link).get();

                            final String fiyat = doc.select("div.product-price-container span.prc-dsc").text();
                            final String isim = doc.select("h1.pr-new-br span").text();

                            System.out.println(isim);
                            System.out.println(fiyat);
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }

                    double cpuUsage = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() / 1000000.0;
                    cpuUsages[index] += cpuUsage;

                    MemoryUsage usedMemory = memoryMXBean.getHeapMemoryUsage();
                    trendyolmemoryUsages[index] += usedMemory.getUsed();
                }
            });

            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        double totalCpuUsage = 0.0;
        for (int i = 0; i < threadCount; i++) {
            totalCpuUsage += cpuUsages[i];
        }


        double totalRamUsage = 0.0;
        for (int i = 0; i < threadCount; i++) {
            totalRamUsage += trendyolmemoryUsages[i];
        }


        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Kodun çalışma süresi: " + totalTime + " ms");
        System.out.println("CPU Kullanımı: " + totalCpuUsage + " ms");
        System.out.println("Ram Kullanımı: " + totalRamUsage + " ms");
    }
}
