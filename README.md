<h1 align="center"> Tugas Kecil 3 IF2211 Strategi Algoritma </h1>
<h1 align="center">Penyelesaian Puzzle Rush Hour Menggunakan Algoritma Pathfinding</h1>

![image](https://github.com/user-attachments/assets/ac2d911c-6245-4638-a1f1-dcab4a797caa)
Puzzle game di atas memang relatif mudah untuk ukuran yang solveable bagi manusia seperti papan berukuran 6x6 dengan jumlah mobil 12. Tetapi bayangkan jika puzzle berukuran 100x100 dengan jumlah piece ribuan, apakah anda bisa menyelesaikannya? Tingkat kesulitan bertumbuh eksponensial terhadap ukuran dan jumlah hanya komputer yang dapat melakukan tugas repetitif untuk menyelesaikannya dengan memanfaatkan algoritma pathfinding dengan cepat.
Program ini mengimplementasikan algoritma pathfinding seperti A*, Uniform Cost Search, dan Greedy Best First Search untuk menyelesaikan puzzle game rush hour. Instruksi penggunaan program dapat dilihat di bawah.

## Project Structure
```bash
RushHourSolver/
├── bin/
├── doc/
│   └── .gitkeep
├── src/
│   ├── Algorithm.java
│   ├── Board.java
│   ├── Direction.java
│   ├── Heuristic.java
│   ├── IOHandler.java
│   ├── Main.java
│   ├── Orientation.java
│   ├── Piece.java
│   ├── Solver.java
│   └── State.java
├── test/
│   ├── .gitkeep
│   ├── 1.txt
│   ├── 2.txt
│   ├── 3.txt
│   ├── 4.txt
│   ├── 5.txt
│   └── input.txt
├── .gitignore
├── Makefile
└── README.md
```


## Command Run Program
1. Clone repository ini
2. Masuk ke directory project ini
    ```bash
    cd path/to/Tucil3_13523034/13523056
    ```
4. Lakukan build:
    ```bash
    make all
    make run
    ```
5. Program dapat dijalankan, ikuti instruksi program seperti
   - Masukkan path to test case
   - Pilih algoritma dan heuristik
6. Test merupakan file txt dan memiliki format penulisan seperti pada contoh tersedia


## Authors
### **Kelompok "APA AJA UDAH"**
|   NIM    |                  Nama                  |
| :------: | :-------------------------------------:|
| 13523034 |         Rafizan M Syawalazmi           |
| 13523056 |               Salman Hanif             |
