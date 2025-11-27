package com.adaptavist.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaApplication

class RatpackGroovyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getPlugins().apply('io.ratpack.ratpack-java')
        project.getPlugins().apply(GroovyPlugin.class)

        project.dependencies {
            implementation "io.ratpack:ratpack-groovy:2.0.0-rc-1", {
                exclude group: "org.codehaus.groovy", module: "groovy-all"
                exclude group: "org.codehaus.groovy", module: "groovy"
                exclude group: "org.codehaus.groovy", module: "groovy-xml"
                exclude group: "org.codehaus.groovy", module: "groovy-sql"
                exclude group: "org.codehaus.groovy", module: "groovy-templates"
            }
        }

        project.dependencies {
            testImplementation "io.ratpack:ratpack-groovy-test:2.0.0-rc-1", {
                exclude group: "org.codehaus.groovy", module: "groovy-all"
                exclude group: "org.codehaus.groovy", module: "groovy"
                exclude group: "org.codehaus.groovy", module: "groovy-xml"
                exclude group: "org.codehaus.groovy", module: "groovy-sql"
                exclude group: "org.codehaus.groovy", module: "groovy-templates"
            }
        }
    }
}
