package com.test.digitec.core.mapper

interface Mapper<InputType, OutputType> {
    fun map(inputModel: InputType): OutputType
}