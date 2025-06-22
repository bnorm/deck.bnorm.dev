package dev.bnorm.dcnyc25.template

fun <T> start(transformer: () -> T): List<T> {
    return listOf(transformer())
}

fun <T> List<T>.then(transformer: T.() -> T): List<T> {
    return this + transformer(this.last())
}
