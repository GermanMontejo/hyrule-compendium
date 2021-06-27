package com.ebookfrenzy.hyrule.model

data class AllEntries(
    val creatures: Creature,
    val equipment: List<Equipment>,
    val materials: List<Material>,
    val monsters: List<Monster>,
    val treasure: List<Treasure>
)