# Shimori
**Work-in-progress** Experiments with new stack  
After: Reincarnation of Shikimori App

## Setup
You require the latest [Android Studio Arctic Fox](https://developer.android.com/studio) (or newer) to be able to build the app.

### API keys

You need to add client key for [Shikimori](https://shikimori.one/oauth)

When creating a OAuth Shikimori app, you need to set the OAuth redirect uri to `shimori://oauth/shikimori`.

Once you obtain the keys, set them in `~/.gradle/gradle.properties`:

```
# Get these from https://shikimori.one/oauth
# ShikimoriClientId = <your key>
# ShikimoriClientSecret = <your key>
```

Inspired by [Tivi](https://github.com/chrisbanes/tivi)
