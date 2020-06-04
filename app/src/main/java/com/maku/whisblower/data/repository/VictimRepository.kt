package com.maku.whisblower.data.repository

class VictimRepository {
    var instance: VictimRepository? = null
        get() {
            if (field == null) {
                field = VictimRepository()
            }
            return field
        }
        private set
}
