import { GameObject } from "./GameObject";
import { Block } from "./Block";
import { Snake } from "./Snake";

export class GameMap extends GameObject {
    constructor(ctx, parent) {
        super(); // super class constructor

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; // absolute distance (L is the atomic unit)

        // row and col
        this.rows = 13;
        this.cols = 14;

        this.inner_blocks_count = 20;// record blocks number
        // store all the blocks
        this.blocks = [];

        this.snakes = [
            new Snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
            new Snake({ id: 1, color: "#F94848", r: 1, c: this.cols - 2 }, this),
        ];
    }

    
    // leetcode flood fill ;)
    // check if the graph is connected i.e. no
    // input: blocks positions, start (x, y), end(x, y)
    check_connectivity(g, sx, sy, tx, ty) {
        // found a way to connect
        if (sx == tx && sy == ty) {
            return true;
        }
    
        // visited
        g[sx][sy] = true; 
    
        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        
        // check four directions 
        for (let i = 0; i < 4; i++) {
            let x = sx + dx[i];
            let y = sy + dy[i];
            // js automatically handle index out of bound
            if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty)) {
                return true;
            }
        }
        // cannot find a path
        return false;
    }

    // create surrounding blocks and inner blocks
    create_blocks() {
        const g = []; // check if a block has been placed
        // intialize
        for (let r = 0; r < this.rows; r++) {
            g[r] = [];
            for (let c = 0; c < this.cols; c++) {
                g[r][c] = false; 
            }
        }

        // Add blocks to surroundings
        // left and right
        for (let r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = true;
        }
        // top and bottom
        for (let c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = true;
        }

        // randomly add inner blocks，put two at one time
        // central symmetric
        for (let i = 0; i < this.inner_blocks_count / 2; i++) {
            // run 1000 times to find an empty place
            for (let j = 0; j < 1000; j++) {
                let r = parseInt(Math.random() * this.rows); 
                let c = parseInt(Math.random() * this.cols);
                // check if this position is occupied and also its symmetric position
                if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) {
                    continue;
                }
                // if randomly chose the spawning points of two snakes
                if (r == this.rows - 1 && c == 1 || r == 1 && c == this.cols - 2) {
                    continue;
                }
                // record
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true;
                break;
            }
        }
        // use JSON parse and stringify to deep copy
        const copy_g = JSON.parse(JSON.stringify(g));

        // check if the map is connected
        if (!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) {
            return false;
        }

        // add blocks
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c] == true) {
                    this.blocks.push(new Block(r, c, this));
                }
            }
        }
        return true;
    }

    // helper function to capture keyboard input
    add_listening_events() {

        this.ctx.canvas.focus();

        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            if (e.key === 'w') snake0.set_direction(0);
            else if (e.key === 'd') snake0.set_direction(1);
            else if (e.key === 's') snake0.set_direction(2);
            else if (e.key === 'a') snake0.set_direction(3);
            else if (e.key === 'ArrowUp') snake1.set_direction(0);
            else if (e.key === 'ArrowRight') snake1.set_direction(1);
            else if (e.key === 'ArrowDown') snake1.set_direction(2);
            else if (e.key === 'ArrowLeft') snake1.set_direction(3);
        });

    }

    start() {
        // brutal force to search for a valid game map
        for (let i = 0; i < 1000; i++) {
            if (this.create_blocks()) break;
        }
        this.add_listening_events();
    }

    update_size() {
        // update size every frame
        // parseInt to avoid white lines
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        // calculate canvas size
        this.ctx.canvas.width = this.L * this.cols;  // canvas length
        this.ctx.canvas.height = this.L * this.rows; // canvas width
    }

    check_ready() {
        for (const snake of this.snakes) {
            // either die or move
            if (snake.status !== "idle") return false;
            // no movement 
            if (snake.direction === -1) return false;
        }
        // ok
        return true;
    }

    update() {
        // execute every frame
        // calculate box size
        this.update_size();
        // when two bots are ready, take next step
        if (this.check_ready()) {
            this.next_step();
        }
        // render every frame
        this.render();
    }

    next_step() {
        // move to next step
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }

    // check if target cell is legal
    // 1. not the body of any snake
    // 2. not blocks
    check_valid(cell) {
        for (const block of this.blocks) {
            if (block.r === cell.r && block.c === cell.c) {
                return false;
            } 
        } 

        // enumerate two snakes body
        for (const snake of this.snakes) {
            // check if tail is going to shrink
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) {
                k--;
            }

            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c) {
                    return false;
                }
            }
        }
        return true;
    }

    render() {
        // different color for neighbor squares
        const color_even = "#AAD751", color_odd = "#A2D149";
        // r for row, c for col
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                // even
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                // draw small squares
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
}