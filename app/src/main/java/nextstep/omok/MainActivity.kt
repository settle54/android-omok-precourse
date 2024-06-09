package nextstep.omok

import android.os.Bundle
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children

enum class Player {
    BLACK, WHITE, NONE
}

class MainActivity : AppCompatActivity() {
    var currentPlayer = Player.BLACK
    val boardSize = 15
    var boardState = Array(boardSize) { Array(boardSize) { Player.NONE } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBoard()
    }

    /**
     * 보드를 설정하는 메소드.
     */
    fun setupBoard() {
        val board = findViewById<TableLayout>(R.id.board)
        for (rowIndex in 0 until boardSize) {
            val tableRow = board.getChildAt(rowIndex) as TableRow
            for (columnIndex in 0 until boardSize) {
                val imageView = tableRow.getChildAt(columnIndex) as ImageView
                imageView.setOnClickListener { putStones(rowIndex, columnIndex) }
            }
        }
    }

    /**
     * 돌을 놓을 때 이미지를 표시하는 메소드.
     * putStones() 메소드에서 사용된다.
     */
    fun updateImage(rowIndex: Int, columnIndex: Int) {
        val cell = (findViewById<TableLayout>(R.id.board).getChildAt(rowIndex) as TableRow)
            .getChildAt(columnIndex) as ImageView
        val resource = when (currentPlayer) {
            Player.BLACK -> R.drawable.black_stone
            Player.WHITE -> R.drawable.white_stone
            else -> return
        }
        cell.setImageResource(resource)
        cell.isEnabled = false
    }

    /**
     * 돌을 놓는 메소드.
     * 돌을 놓을 때마다 승리 여부를 확인한 후,
     * 한 쪽이 승리하면 메시지를 띄우고 게임을 다시 시작한다.
     */
    fun putStones(rowIndex: Int, columnIndex: Int) {
        if (boardState[rowIndex][columnIndex] == Player.NONE) {
            boardState[rowIndex][columnIndex] = currentPlayer
            updateImage(rowIndex, columnIndex)
            if (checkWin(rowIndex, columnIndex)) {
                endGame()
            } else {
                changePlayer()
            }
        }
    }

    /**
     * 주어진 방향으로 연속된 돌의 개수를 세는 메소드.
     * checkWin() 메소드에서 사용된다.
     */
    fun countStones(row: Int, col: Int, dRow: Int, dCol: Int): Int {
        var count = 0
        var r = row + dRow
        var c = col + dCol
        while (r in 0 until boardSize && c in 0 until boardSize && boardState[r][c] == currentPlayer) {
            count++
            r += dRow
            c += dCol
        }
        return count
    }

    /**
     * 돌을 놓은 위치에서 승리 여부를 확인하는 메소드.
     * 수평, 수직, 대각선 방향으로 연속된 돌의 개수를 구한 후,
     * 한 방향에서 연속된 돌의 개수가 5 이상이면 승리로 간주한다.
     */
    fun checkWin(rowIndex: Int, columnIndex: Int): Boolean {
        val directions = arrayOf(
            Pair(1, 0), Pair(0, 1), Pair(1, 1), Pair(1, -1)
        )

        for ((dx, dy) in directions) {
            val count = 1 + countStones(rowIndex, columnIndex, dx, dy) +
                    countStones(rowIndex, columnIndex, -dx, -dy)
            if (count >= 5) {
                return true
            }
        }
        return false
    }

    /**
     * 플레이어를 전환하는 메소드.
     */
    fun changePlayer() {
        currentPlayer = if (currentPlayer == Player.BLACK) Player.WHITE else Player.BLACK
    }

    /**
     * 보드를 초기화하는 메소드.
     */
    fun resetBoard() {
        boardState = Array(boardSize) { Array(boardSize) { Player.NONE } }
        findViewById<TableLayout>(R.id.board).children
            .filterIsInstance<TableRow>()
            .forEach { tableRow ->
                tableRow.children.filterIsInstance<ImageView>().forEach { imageView ->
                    imageView.apply {
                        setImageDrawable(null)
                        isEnabled = true
                    }
            }
        }
        currentPlayer = Player.BLACK
    }

    /**
     * 게임 종료를 알리는 메소드.
     * 게임 결과를 메시지로 띄우고 게임을 초기화한다.
     */
    fun endGame() {
        Toast.makeText(this, "${currentPlayer.name} 승리", Toast.LENGTH_LONG).show()
        resetBoard()
    }

}