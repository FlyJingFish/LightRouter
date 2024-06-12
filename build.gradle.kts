// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.module.communication.plugin)
    }
}
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.ksp.plugin) apply false
    alias(libs.plugins.androidAop.plugin) apply true
}
true // Needed to make the Suppress annotation work for the plugins block

ext {
    set("sdkVersion",31)
    set("minSdkVersion",21)
}

fun getVersionProperty(propName:String, defValue:String):String {
    val file = file("version.properties")
    var ret = defValue
    if (file.exists() && file.canRead()) {
        val input = java.io.FileInputStream(file)
        val props = java.util.Properties()
        props.load(input)
        ret = props[propName].toString()
        input.close()
    }
    return ret
}


fun getAppVName(key :String): String {
    return getVersionProperty(key, "1.0.0")
}


fun getAppVCode(key :String):Int {
    val versionName = getAppVName(key)
    val versions = versionName.split(".")
    var updateVersionString = ""
    for ((i, item) in versions.withIndex()) {
        val subString = item
        if (i == 0) {
            updateVersionString += subString
            continue
        } else if (i >= 3) {
            break
        }
        val subNumber = Integer.parseInt(subString)
        updateVersionString += String.format("%01d", subNumber)
    }
    return updateVersionString.toInt()
}

task ("bumpVersion") {
    doLast {
        updateVersion()
    }
}

fun updateVersion(){
    val comVersionName = getAppVName("COMMUNICATION_VERSION")
    val aopVersionName = getAppVName("ANDROID_VERSION")


    updateREADME("README.md",aopVersionName,comVersionName)

    println("升级版本号完成，AndroidAOP versionName = $aopVersionName, ModuleCommunication versionName = $comVersionName")
}



fun updateREADME(readme :String, aopVersionName :String, comVersionName :String) {
    val oldVersionName = ".*?"
    val configFile = File(readme)
    val exportText = configFile.readText()
    var text = exportText.updateText("'io.github.FlyJingFish.ModuleCommunication:module-communication-plugin:$oldVersionName'",
        "'io.github.FlyJingFish.ModuleCommunication:module-communication-plugin:$comVersionName'"
    )
    text = text.updateText("'io.github.FlyJingFish.ModuleCommunication:module-communication-annotation:$oldVersionName'",
        "'io.github.FlyJingFish.ModuleCommunication:module-communication-annotation:$comVersionName'"
    )
    text = text.updateText("'io.github.FlyJingFish.ModuleCommunication:module-communication-intercept:$oldVersionName'",
        "'io.github.FlyJingFish.ModuleCommunication:module-communication-intercept:$comVersionName'"
    )
    text = text.updateText("'io.github.FlyJingFish.ModuleCommunication:module-communication-route:$oldVersionName'",
        "'io.github.FlyJingFish.ModuleCommunication:module-communication-route:$comVersionName'"
    )

    text = text.updateText("'io.github.FlyJingFish.AndroidAop:android-aop-plugin:$oldVersionName'",
        "'io.github.FlyJingFish.AndroidAop:android-aop-plugin:$aopVersionName'"
    )
    text = text.updateText("'io.github.FlyJingFish.AndroidAop:android-aop-core:$oldVersionName'",
        "'io.github.FlyJingFish.AndroidAop:android-aop-core:$aopVersionName'"
    )
    text = text.updateText("'io.github.FlyJingFish.AndroidAop:android-aop-annotation:$oldVersionName'",
        "'io.github.FlyJingFish.AndroidAop:android-aop-annotation:$aopVersionName'"
    )
    text = text.updateText("'io.github.FlyJingFish.AndroidAop:android-aop-processor:$oldVersionName'",
        "'io.github.FlyJingFish.AndroidAop:android-aop-processor:$aopVersionName'"
    )
    text = text.updateText("'io.github.FlyJingFish.AndroidAop:android-aop-ksp:$oldVersionName'",
        "'io.github.FlyJingFish.AndroidAop:android-aop-ksp:$aopVersionName'"
    )
    text = text.updateText("id 'io.github.FlyJingFish.AndroidAop.android-aop' version '$oldVersionName'",
        "id 'io.github.FlyJingFish.AndroidAop.android-aop' version '$aopVersionName'"
    )

    configFile.writeText(text)
}

fun String.updateText(regex:String,newVersionText:String):String{
    val text:String = this
    val versionPattern: java.util.regex.Pattern =
        java.util.regex.Pattern.compile(regex)
    val versionMatcher: java.util.regex.Matcher = versionPattern.matcher(text)
    return if (versionMatcher.find()) {
        versionMatcher.replaceAll(newVersionText)
    } else {
        text
    }
}