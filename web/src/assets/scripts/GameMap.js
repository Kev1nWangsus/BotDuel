import { GameObject } from "./GameObject";
import { Block } from "./Block";
import { Snake } from "./Snake";

export class GameMap extends GameObject {
    constructor(ctx, parent, store) {
        super(); // super class constructor

        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; // absolute distance (L is the atomic unit)
        this.store = store;
        
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

    // create surrounding blocks and inner blocks
    create_blocks() {
        const g = this.store.state.pk.gamemap;

        // add blocks
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.blocks.push(new Block(r, c, this));
                }
            }
        }
        return true;
    }

    // helper function to capture keyboard input
    add_listening_events() {
        if (this.store.state.record.is_record) {
            let k = 0;

            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser = this.store.state.record.record_loser;
            const [snake0, snake1] = this.snakes;
            const interval_id = setInterval(() => {
                // remove last step since it's death moment
                if (k >= a_steps.length - 1) {
                    if (loser === "all" || loser === "A") {
                        snake0.status = "die";
                    }
                    if (loser === "all" || loser === "B") {
                        snake1.status = "die";
                    }
                    clearInterval(interval_id);
                } else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++;
            }, 300);
        } else {
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;
                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1;
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;

                if (d >= 0) {
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d,
                    }));
                }
            });
        }
    }

    start() {
        this.create_blocks();
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