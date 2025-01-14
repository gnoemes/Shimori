package com.gnoemes.shimori.tasks

import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

expect interface TasksPlatformComponent


@ContributesTo(AppScope::class)
interface TasksComponent : TasksPlatformComponent {
}
