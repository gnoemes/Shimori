# Shimori
**Work-in-progress** Experiments with new stack  
After: Reincarnation of Shikimori App

## Setup
You require [Android Studio Ladybug | 2024.2.1 Patch 2](https://developer.android.com/studio/archive) to be able to build the app.

### API keys

You need to add client key for [Shikimori](https://shikimori.me/oauth)

When creating a OAuth Shikimori app, you need to set the OAuth redirect uri for desktop platform only.

Once you obtain the keys, set them in `~/source-bundled/shikimori/gradle.properties`:

```
# Get these from https://shikimori.me/oauth
# ShikimoriClientId = <your key>
# ShikimoriClientSecret = <your key>
# ShikimoriRedirectUrl = <your uri>

# For desktop
# ShikimoriClientIdDesktop = <your key>
# ShikimoriClientSecretDesktop = <your key>
```

Inspired by [Tivi](https://github.com/chrisbanes/tivi)
