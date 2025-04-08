package com.gnoemes.shimori.common.imageloading

import coil3.request.ImageRequest

expect fun ImageRequest.Builder.allowHardwareSafe(enable: Boolean) : ImageRequest.Builder