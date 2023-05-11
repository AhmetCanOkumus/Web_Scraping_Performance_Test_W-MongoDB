package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Timer;
import java.util.TimerTask;

public class TrendyolVatanTest {

    public static void main(String[] args) throws InterruptedException {
        int trendyolThreadCount = 2;
        int vatanThreadCount = 2;

        Thread[] trendyolThreads = new Thread[trendyolThreadCount];
        Thread[] vatanThreads = new Thread[trendyolThreadCount];
        double[] cpuUsages = new double[trendyolThreadCount];
        double[] trendyolmemoryUsages = new double[trendyolThreadCount];
        double[] vatanmemoryUsages = new double[vatanThreadCount];
        double[] vatancpuUsages = new double[vatanThreadCount];
        MemoryMXBean memoryMXBean=ManagementFactory.getMemoryMXBean();
        long startTime = System.currentTimeMillis();
        long startTime2 = System.nanoTime();


                Dbo dboTrendyol=new Dbo();
                Dbo dboVatan=new Dbo();

                for (int i = 0; i < trendyolThreadCount; i++) {
                    final int index = i;

                    trendyolThreads[i] = new Thread(() -> {
                        for (int j = 2 + index; j < 5; j += trendyolThreadCount) {
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


                                    if(dboTrendyol.checkIfRecordExists(isim,fiyat)==false){
                                        dboTrendyol.insert(isim,fiyat);
                                    }

                                    if(dboTrendyol.checkIfRecordExists(isim,fiyat)==true) {
                                        String eskiFiyat =   dboTrendyol.getFiyat(isim);

                                        if (eskiFiyat != fiyat) {
                                            dboTrendyol.updateFiyat(isim,fiyat);
                                        }

                                    }

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

                    trendyolThreads[i].start();
                }



                for (int i = 0; i < vatanThreadCount; i++) {
                    final int index = i;
                    vatanThreads[i] = new Thread(() -> {
                        for (int j = 2 + index; j < 5; j += vatanThreadCount) {
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


                                    if(dboVatan.checkIfRecordExists(isim,fiyat)==false){
                                        dboVatan.insert(isim,fiyat);
                                    }

                                    if(dboVatan.checkIfRecordExists(isim,fiyat)==true) {
                                        String eskiFiyat =   dboVatan.getFiyat(isim);

                                        if (eskiFiyat != fiyat) {
                                            dboVatan.updateFiyat(isim,fiyat);
                                        }

                                    }

                                }
                            } catch (IOException e) {
                                System.err.println(e.getMessage());
                            }

                            double cpuUsage = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime() / 1000000.0;
                            vatancpuUsages[index] += cpuUsage;

                            MemoryUsage usedMemory = memoryMXBean.getHeapMemoryUsage();
                            vatanmemoryUsages[index] += usedMemory.getUsed();
                        }
                    });

                    vatanThreads[i].start();
                }




        for (int i = 0; i < trendyolThreadCount; i++) {
            trendyolThreads[i].join();
        }

        for (int i = 0; i < vatanThreadCount; i++) {
            vatanThreads[i].join();
        }

        double totalCpuUsage = 0.0;
        for (int i = 0; i < trendyolThreadCount; i++) {
            totalCpuUsage += cpuUsages[i];
        }

        for (int i = 0; i < vatanThreadCount; i++) {
            totalCpuUsage += vatancpuUsages[i];
        }

        double totalMemoryUsage=0.0;
        for (int i = 0; i < trendyolThreadCount; i++) {
            totalMemoryUsage += trendyolmemoryUsages[i];
        }

        for (int i = 0; i < trendyolThreadCount; i++) {
            totalMemoryUsage += vatanmemoryUsages[i];
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Kodun çalışma süresi: " + totalTime + " ms");
        System.out.println("CPU Kullanımı: " + totalCpuUsage + " ms");
        System.out.println("RAM Kullanımı: " + totalMemoryUsage);



    }
}
