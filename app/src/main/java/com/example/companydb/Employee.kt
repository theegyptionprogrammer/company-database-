package com.example.companydb


class Employee {

    var name: String = ""
    var address: String = ""
    var position: String = ""
    var id: Int = 0
    var url : String = ""

    constructor(name : String , address : String , position : String , url : String , id : Int){
        this.name = name
        this.address = address
        this.position = position
        this.url = url
        this.id = id
    }

    constructor(name : String , address : String , position : String ,  id : Int){
        this.name = name
        this.address = address
        this.position = position
        this.id = id
    }

    constructor(name: String){
        this.name = name
    }




}
