plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "name.denyago.tcpsrvz.rapidoid.Main"
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = application.mainClassName
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
    compile("org.rapidoid:rapidoid-net:5.5.5")
}
