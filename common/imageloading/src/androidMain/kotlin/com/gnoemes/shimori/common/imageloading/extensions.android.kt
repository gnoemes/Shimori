package com.gnoemes.shimori.common.imageloading

import coil3.request.ImageRequest
import coil3.request.allowHardware


actual fun ImageRequest.Builder.allowHardwareSafe(enable: Boolean) = allowHardware(enable)