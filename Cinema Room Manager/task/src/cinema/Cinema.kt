package cinema

fun getInitialInput(): List<Int>{
    println("Enter the number of rows:")
    val numRows: Int = readln().toInt()
    println("Enter the number of seats in each row:")
    val numSeatsPerRow: Int = readln().toInt()
    return listOf(numRows, numSeatsPerRow)
}

fun createTheatreModel(rows: Int, seatsPerRow: Int): MutableList<MutableList<Char>> {
    val theatreModel: MutableList<MutableList<Char>> = MutableList(rows) { rowidx -> MutableList(seatsPerRow) { seatidx -> ' ' } }
    for (row in 0 until rows) {
        for (seat in 0 until seatsPerRow) {
            theatreModel[row][seat] = 'S'
        }
    }
    return theatreModel
}

fun displayTheatre(theatre: MutableList<MutableList<Char>>, rows: Int, seatsPerRow: Int) {
    println("Cinema: ")
    for (seatidx in 0 .. seatsPerRow) {
        if (seatidx == 0) print(' ') else print(" ${seatidx}")
    }
    println()
    for (row in 0 until rows) {
        for (seat in 0 until seatsPerRow) {
            if (seat == 0) print("${row + 1} ${theatre[row][seat]}") else print(" ${theatre[row][seat]}")
        }
        println()
    }
}

fun seatIdxOutOfBounds(theatre: MutableList<MutableList<Char>>, row: Int, seat: Int): Boolean {
    return (row >= theatre.size || seat >= theatre[0].size)
}

fun getBookingDetails(theatre: MutableList<MutableList<Char>>): List<Int> {
    println("\nEnter a row number:")
    var bookRow = readln().toInt() - 1
    println("Enter a seat number in that row:")
    var bookSeat = readln().toInt() - 1
    return listOf(bookRow, bookSeat)
}

fun markSeat(theatre: MutableList<MutableList<Char>>, rowidx: Int, seatidx: Int) {
    theatre[rowidx][seatidx] = 'B'
}

fun getPrice(theatre: MutableList<MutableList<Char>>, rowidx: Int, seatidx: Int, totalNumOfSeats: Int): Int {
    return if (totalNumOfSeats <= 60 || rowidx + 1 <= theatre.size / 2) 10 else 8
}

fun countPurchasedTickets(theatre: MutableList<MutableList<Char>>): Int {
    var sumOfPurchases = 0
    for (row in 0 until theatre.size) {
        for (seat in 0 until theatre[row].size) {
            if (theatre[row][seat] == 'B') sumOfPurchases++
        }
    }
    return sumOfPurchases
}

fun getOccupancy(theatre: MutableList<MutableList<Char>>): Double {
    return (countPurchasedTickets(theatre).toDouble() / (theatre.size * theatre[0].size)) * 100
}

fun getCurrentIncome(theatre: MutableList<MutableList<Char>>): Int {
    var currentIncome = 0
    val totalNumOfSeats = theatre.size * theatre[0].size
    for (row in 0 until theatre.size) {
        for (seat in 0 until theatre[row].size) {
            if (theatre[row][seat] == 'B') {
                if (totalNumOfSeats <= 60 || row <= (theatre.size / 2) - 1) currentIncome += 10 else currentIncome += 8
            }
        }
    }
    return currentIncome
}

fun getTotalIncome(theatre: MutableList<MutableList<Char>>): Int {
    val totalNumOfSeats = theatre.size * theatre[0].size
    return if (totalNumOfSeats <= 60) 10 * totalNumOfSeats else 10 * ((theatre.size / 2) * theatre[0].size) + 8 * (totalNumOfSeats - ((theatre.size / 2) * theatre[0].size))
}

fun showStatistics(theatre: MutableList<MutableList<Char>>) {
    val numPurchased = countPurchasedTickets(theatre)
    val percentage = getOccupancy(theatre)
    val currentIncome = getCurrentIncome(theatre)
    val totalIncome = getTotalIncome(theatre)
    val output = """
        Number of purchased tickets: ${numPurchased}
        Percentage: ${"%.2f".format(percentage)}%
        Current income: $${currentIncome}
        Total income: $${totalIncome}
    """.trimIndent()
    println(output)
}

fun main() {
    val initialInput = getInitialInput()
    val numRows = initialInput[0]
    val numSeatsPerRow = initialInput[1]
    val totalNumOfSeats: Int = numRows * numSeatsPerRow
    val theatre = createTheatreModel(numRows, numSeatsPerRow)
    while (true) {
        println("""
        1. Show the seats
        2. Buy a ticket
        3. Statistics
        0. Exit
    """.trimIndent())
        val userChoice = readln().toInt()
        when (userChoice) {
            0 -> break
            1 -> displayTheatre(theatre, numRows, numSeatsPerRow)
            2 -> {
                var bookingDetails = getBookingDetails(theatre)
                var bookRow = bookingDetails[0]
                var bookSeat = bookingDetails[1]
                while (seatIdxOutOfBounds(theatre, bookRow, bookSeat)) {
                    println("Wrong input!")
                    bookingDetails = getBookingDetails(theatre)
                    bookRow = bookingDetails[0]
                    bookSeat = bookingDetails[1]
                }
                while (theatre[bookRow][bookSeat] == 'B') {
                    println("That ticket has already been purchased!")
                    bookingDetails = getBookingDetails(theatre)
                    bookRow = bookingDetails[0]
                    bookSeat = bookingDetails[1]
                }
                markSeat(theatre, bookRow, bookSeat)
                println("Ticket price: \$${getPrice(theatre, bookRow, bookSeat, totalNumOfSeats)}")

            }
            3 -> showStatistics(theatre)
            else -> {
                println("Wrong Input")
                break
            }
        }
    }
}