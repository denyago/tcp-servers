# Comparison of different TCP Servers

My motivation was to learn network software development. See something outside of HTTP REST that I do all workdays long.

Disclaimer: 

- work in progress
- no tests so far, I'm in experimenting mode
- no single command that will run all
- no Docker or any other deployment
- no thread safety (see the plans below)

Big plans:

- try all server options
- make client really concurrent (coroutines, [connection pools](https://blog.adamgamboa.dev/2020/01/06/creating-a-socket-client-pool-in-java/) or other [connection pools](https://www.freecodecamp.org/news/how-to-implement-an-object-pool-with-an-actor-in-kotlin-ed06d3ba6257/))
- add a Redis instead of in-memory store
- make sure everything in non-blocking and thread safe
- run benchmarks

## Contents

A [multi-project build](https://docs.gradle.org/current/userguide/multi_project_builds.html) of different TCP server implementations, a core of the game and a client:

 1. [rapidoid](./rapidoid) - [doc](https://www.rapidoid.org/documentation.html#implementing-echo-protocol), [example](https://github.com/selvakn/todobackend-kotlin-rapidoid)
 2. [korio](./corio) - [GitHub](https://github.com/korlibs/korio)
 3. [vertx](./vertx) - [doc](https://vertx.io/docs/vertx-core/kotlin/#_writing_tcp_servers_and_clients), [example](https://github.com/fvasco/vertKtDemo)
 4. [netty](./netty) - [article](https://medium.com/@ashertoqeer/netty-simple-tcp-server-51fa8537fad5)
 5. [ktor](./ktor) - [doc](https://ktor.io/servers/raw-sockets.html) 
 6. [core](./core) - server-side game logic
 7. [client](./client) - game client
 
 I've taken the inspiration from [this comparison](https://www.techempower.com/benchmarks/#section=data-r19&hw=cl&test=plaintext&s=1&l=x5nh4v-1r&a=2).

## Running

Build

    ./gradlew clean build
    
Run server (for example):

    java -jar rapidoid/build/libs/rapidoid-1.0.jar
    
Run client:
    
    time java -jar client/build/libs/client-1.0.jar
