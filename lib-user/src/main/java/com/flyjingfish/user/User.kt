package com.flyjingfish.user

import com.flyjingfish.module_communication_annotation.ExposeBean
import java.io.Serializable

@ExposeBean
data class User (val id:String) :Serializable