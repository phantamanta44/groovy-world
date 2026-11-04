plugins {
    id("st.evening.mc.prelude.build") version "1.0.1"
    id("maven-publish")
}

val projectModId: String = "groovyworld"
group = "st.evening.mc.groovyworld"
version = "1.0.0"

preludeBuild {
    modId = projectModId
    mod {
        constantsClass = "GrWConsts"
    }
    minecraft {
        mcVersion = "1.12.2"
        deobf {
            mappingsChannel = "stable"
            mappingsVersion = "39"
        }
    }
}

repositories {
    maven {
        name = "Cleanroom"
        url = uri("https://maven.cleanroommc.com/")
    }
    maven {
        name = "GTCEu"
        url = uri("https://maven.gtceu.com/")
    }
}

dependencies {
    implementation(rfg.deobf("com.cleanroommc:groovyscript:1.4.3:") as String) {
        exclude(group = "mezz.jei")
        exclude(group = "curse.maven")
    }
}
