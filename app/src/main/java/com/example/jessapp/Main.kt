package com.example.jessapp

import android.view.Surface
import java.lang.ProcessBuilder.Redirect.to

class Piece(
    val width: Float,
    val height: Float,
    val name: String,
    surface: Surface
)

class Cuisine {
    val results: List<Piece>
    val width: Double
    val height: Double
    val name: String

    constructor(
        results: List<Piece> = listOf(),
        width: Double = 0.0,
        height: Double = 0.0,
        name: String = ""
    ) {
        this.results = results
        this.width = width
        this.height = height
        this.name = name
    }
}

class Salon(

    val results: List<Piece> = listOf(),
    val width: Double = 5.0,
    val height: Double = 2.0,
    val name: String = ""
)

class Etudiant(val name: String, val promo : String, val matieres: List<String>)
val etudiants = listOf(
    Etudiant("Paul", "2025", listOf("mobile", "web","BDD")),
    Etudiant("Yazid", "2024", listOf("mobile", "Android","Réseau")),
    Etudiant("Caroline", "2025", listOf("SE", "Anglais")),
)

fun main () {
    println ("coucou")
    //println(etudiants:Etudiant to promo = 2024)
   // println(etudiants:Etudiant to matieres > 2)
    //println(etudiants:Etudiant to promo = 2024)

}