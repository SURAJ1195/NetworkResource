package com.example.mynetworkcall

internal val converter: ConverterInterface = ConverterImpl()
internal val handler: HandlerInterface = HandlerImpl()
internal val internetObserver: InternetObserverInterface = InternetObserverImpl()
internal val outerIntent: OuterIntentInterface = OuterIntentImpl()
internal val installer: InstallerInterface = InstallerImpl()