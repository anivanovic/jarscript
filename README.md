# JarScript

JarScript is Gradle plugin which adds single task to your build to distribute your JVM
application as a single bash script file. Running `jarscript` task will create 
bash script in `$buildDir/libs` folder. The bash script is self-contained and does
not need any other dependency to execute you application. 

### Use
To use it in your project add to your build gradle
```
plugins {
    id 'com.github.anivanovic.jarscript' version '0.1.0'
}
```
Once applied, plugin will expose `jarscript` task to your build. This task will
invoke building a jar, create bash script for starting the application and then
package created jar into bash script.

For a plugin to create self-contained bash script we need set up `jar` task to
create executable uber jar. To achive this we need to configure `jar` task

```
jar {
    manifest {
        attributes(
            'Main-Class': 'com.package.to.your.MainClass'
        )
    }
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
}
``` 

This sets up `jar` task to package all needed dependencies inside our jar and populate
Manifest file with correct class to run when executed.

### Contribute
Contributions are welcomed, and you are encouraged to submit pull request :)