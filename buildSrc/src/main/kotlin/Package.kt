object Package {
    const val group = "com.twidere"
    const val name = "Tweets"
    const val id = "$group.twiderex"
    val versionName =
        "${Version.main}.${Version.mirror}.${Version.patch}${if (Version.revision.isNotEmpty()) "-${Version.revision}" else ""}"
    const val copyright = "Copyright (C) TweetsProject and Contributors"
    const val versionCode = Version.build
    const val applicationId = "com.tweet.android.twitter_c"
    object Version {
        const val main = "1"
        const val mirror = "0"
        const val patch = "1"
        const val revision = "4"
        const val build = 4
    }
}
