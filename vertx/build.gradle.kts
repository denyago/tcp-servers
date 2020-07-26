plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "io.vertx.core.Launcher"
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "io.vertx.core.Launcher"
        attributes["Main-Verticle"] = "name.denyago.tcpsrvz.vertx.GameServer"
    }

    // To add all of the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}

dependencies {
    compile(project(":core"))
    compile(kotlin("stdlib"))
    compile("io.vertx:vertx-core:3.9.2")
    compile("io.vertx:vertx-lang-kotlin:3.9.2")
}
