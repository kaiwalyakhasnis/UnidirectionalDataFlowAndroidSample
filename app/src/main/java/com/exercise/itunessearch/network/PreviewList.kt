package com.exercise.itunessearch.network

import com.google.gson.annotations.SerializedName

data class PreviewList(

	@field:SerializedName("resultCount")
	val resultCount: Int? = null,

	@field:SerializedName("results")
	val results: List<PreviewItem?>? = null
)
