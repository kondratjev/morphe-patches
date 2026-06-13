group = "app.morphe"

patches {
    about {
        name = "Kondratjev Patches"
        description = "Patches for Lyfta and other apps"
        source = "git@github.com:kondratjev/morphe-patches.git"
        author = "kondratjev"
        contact = "kondratj3v@gmail.com"
        website = "https://github.com/kondratjev/morphe-patches"
        license = "GPLv3"
    }
}

dependencies {
    // Used by JsonGenerator.
    implementation(libs.gson)

    // Required due to smali, or build fails. Can be removed once smali is bumped.
    implementation(libs.guava)

    implementation(libs.morphe.patches.library)

    compileOnly(project(":patches:stub"))
}

tasks {
    register<JavaExec>("generatePatchesList") {
        description = "Build patch with patch list"

        dependsOn(build)

        classpath = sourceSets["main"].runtimeClasspath
        mainClass.set("app.morphe.util.PatchListGeneratorKt")
    }
    // Used by gradle-semantic-release-plugin.
    publish {
        dependsOn("generatePatchesList")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xcontext-parameters")
    }
}
