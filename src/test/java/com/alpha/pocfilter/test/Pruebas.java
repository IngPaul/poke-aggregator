package com.alpha.pocfilter.test;

import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@ExtendWith(MockitoExtension.class)
public class Pruebas {
    @Test
    void pruebaCompletableFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<String> name = CompletableFuture.supplyAsync(() -> "Baeldung");

        CompletableFuture<Integer> nameLength = name.thenApply(value -> {
            return value.length();
        });

        Assertions.assertEquals(nameLength.get(), 8);
    }
    @Test
    void pruebaPublisherOn() throws ExecutionException, InterruptedException {
        String threadId1 = String.valueOf(Thread.currentThread().getId());
        System.out.println("pruebaPublisherOn: " + threadId1);
        Flux.fromIterable(Arrays.asList(0,100))
                .flatMap(n->getMono().subscribeOn(Schedulers.parallel()).map(price-> {
                    String threadId01 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("getMono(): " + threadId01);
                    return new Pair<>(n, price);
                }))
                .map(n->{
                    String threadId2 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("map: " + threadId2);
                    return n.getValue0() *n.getValue1();
                })
                .publishOn(Schedulers.parallel())
                .collectList()
                .map(res->{
                    String threadId3 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("map despues de collectList: " + threadId3);
                    System.out.println(res.toString());
                    return res;
                }).subscribe(r->{
                    String threadId3 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("subscribe: " + threadId3);
                });
        String threadId400 = String.valueOf(Thread.currentThread().getId());
        System.out.println("xxxxxxxxxxxxxxxxxxxxxx: " + threadId400);

    }
    @Test
    void pruebaPublisherOn2() throws ExecutionException, InterruptedException {
        String threadId1 = String.valueOf(Thread.currentThread().getId());
        System.out.println("pruebaPublisherOn: " + threadId1);
        Flux.fromIterable(Arrays.asList(0,100))
                .flatMap(n->getMono().map(price-> {
                    String threadId01 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("getMono(): " + threadId01);
                    return new Pair<>(n, price);
                }))
                .map(n->{
                    String threadId2 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("map: " + threadId2);
                    return n.getValue0() *n.getValue1();
                })
                .publishOn(Schedulers.parallel())
                .collectList()
                .map(res->{
                    String threadId3 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("map despues de collectList: " + threadId3);
                    System.out.println(res.toString());
                    return res;
                }).subscribe(r->{
                    String threadId3 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("subscribe: " + threadId3);
                });
        String threadId400 = String.valueOf(Thread.currentThread().getId());
        System.out.println("xxxxxxxxxxxxxxxxxxxxxx: " + threadId400);

    }
    @Test
    public void test(){
        Consumer<Integer> consumer = s -> System.out.println(s + " : " + Thread.currentThread().getName());

        Flux.range(1, 5)
                .doOnNext(consumer)
                .map(i -> {
                    System.out.println("Inside map the thread is " + Thread.currentThread().getName());
                    return i * 10;
                })
                .publishOn(Schedulers.newSingle("First_PublishOn()_thread"))
                .doOnNext(consumer)
                .publishOn(Schedulers.newSingle("Second_PublishOn()_thread"))
                .doOnNext(consumer)
                .subscribeOn(Schedulers.newSingle("subscribeOn_thread"))
                .subscribe();
    }
    Mono<Integer> getMono()
    {
        return Mono.just(2);
    }


    @Test
    void complete() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1
                = CompletableFuture.supplyAsync(() -> {
                        System.out.println("future1 thread is " + Thread.currentThread().getName());
                        return "Hello";
                    });
        CompletableFuture<String> future2
                = CompletableFuture.supplyAsync(() -> {
                        System.out.println("future2 thread is " + Thread.currentThread().getName());
                        return "Beautiful";
                    });
        CompletableFuture<String> future3
                = CompletableFuture.supplyAsync(() ->  {
                        System.out.println("future3 thread is " + Thread.currentThread().getName());
                        return "World";
                    });

        CompletableFuture<Void> combinedFuture
                = CompletableFuture.allOf(future1, future2, future3);

        combinedFuture.get();

        Assertions.assertTrue(future1.isDone());
        Assertions.assertTrue(future2.isDone());
        Assertions.assertTrue(future3.isDone());
    }
    @Test
    void transformFluxToComplete(){
            // Ejemplo de Flux que emite una lista de elementos
            Flux<Integer> flux = Flux.range(1, 50000);

            // Convertir el Flux a CompletableFuture
            CompletableFuture<List<Integer>> completableFuture = flux.subscribeOn(Schedulers.parallel()).collectList().toFuture()
                    .thenApplyAsync(r->{
                        System.out.println("thenAccept thread is " + Thread.currentThread().getName());
                        return r;
                    });

            // Utilizar el CompletableFuture
            completableFuture.thenAccept(n-> {
                        System.out.println(n);
                    })
                    .join(); // Esperar a que el CompletableFuture se complete

            // Opcionalmente, puedes manejar excepciones
            completableFuture.exceptionally(throwable -> {
                System.err.println("Ocurrió un error: " + throwable.getMessage());
                return null;
            }).join();
    }
    @Test
    void convertionInternal(){
        Flux.range(0,1000)
                .map(price-> {
                    String threadId01 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("*: " + threadId01);
                    return price*2;
                })
                .map(m->{
                    String threadId01 = String.valueOf(Thread.currentThread().getId());
                    System.out.println("<2>: " + threadId01);
                    return m*m;
                })
                .subscribe();
    }

    private static CompletableFuture<List<Integer>> recursiveProcessing(int[] prices, int index, ExecutorService executorService) {
        if (index < prices.length) {
            int price = prices[index];
            return CompletableFuture.supplyAsync(() -> {
                        String threadId01 = String.valueOf(Thread.currentThread().getId());
                        System.out.println("Operation con price:"+price+" e hilo: " + threadId01);
                        int multipliedPrice = price * 2;
                        int result = multipliedPrice * multipliedPrice;
                        // Haz algo con el resultado

                        return result;
                    }, executorService)
                    .thenComposeAsync(result -> recursiveProcessing(prices, index + 1, executorService)
                            .thenApply(subResults -> {
                                subResults.add(result);
                                return subResults;
                            }));
        } else {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }
    private int[] getArray(int length){
        int[] array = new int[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            array[i] = random.nextInt(100);
        }
        return array;
    }

    @Test
    void convertionInternal2() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        int[] prices =getArray(10000000);

        CompletableFuture<List<Integer>> completableFuture = recursiveProcessing(prices, 0, executorService);

        List<Integer> results = completableFuture.join();
        System.out.println("Results: " + results);

        executorService.shutdown();
    }
    @Test
    void test2(){
        Flux.just(5, 10, 23, 25).log()
                .flatMap(n -> Flux.just(n * 2))
                .subscribe(System.out::println);
    }

    @Test
    void test3() throws InterruptedException {
        Flux.just(5, 10, 23, 25).log()
                .subscribeOn(Schedulers.parallel())
                .flatMap(n -> Flux.just(n * 2))
                .subscribe(value -> System.out.println(Thread.currentThread().getId() +"..."+Thread.currentThread().getName() + " : " + value));
        Thread.sleep(10000);
    }

    @Test
    void test4() throws InterruptedException {
        Flux.just(5, 10, 23, 25)
                .map(r1->{
                    System.out.println("map del flux principal: id:"+Thread.currentThread().getId() +"/ name:"+Thread.currentThread().getName());

                    return r1;
                })
                .flatMap(n -> {
                            System.out.println("flatmap del flux principal: id:"+Thread.currentThread().getId() +"/ name:"+Thread.currentThread().getName());

                            return Flux.just(n * 2)
                            .map(r -> {
                                System.out.println("map del flux interno id:" + Thread.currentThread().getId() + "/ name:" + Thread.currentThread().getName());
                                return r;
                            });
                }
                )
                .subscribe();
    }
}
