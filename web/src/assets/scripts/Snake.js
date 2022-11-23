import { GameObject } from './GameObject';
import { Cell } from './Cell';

export class Snake extends GameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c)]; // store snake's position
        this.next_cell = null; // next destination
        this.speed = 5;
        this.direction = -1; // 0: up, 1: right, 2: down, 3: left, -1: none
        this.status = "idle";// { idle，move，die }

        // movement
        this.dr = [-1, 0, 1, 0]; // row 
        this.dc = [0, 1, 0, -1]; // col

        this.step = 0;      // number of steps
        this.eps = 1e-2;    // tolerance

        this.eye_direction = 0; // define initial eye direction: first snake has default upwards direction
        if (this.id === 1) this.eye_direction = 2; // the second snake has default downwards direction

        this.eye_dx = [
            [-1, 1],    // up
            [1, 1],     // right
            [1, -1],    // down
            [-1, -1],   // left

        ];
        this.eye_dy = [
            [-1, -1],   // up
            [-1, 1],    // right
            [1, 1],     // down
            [1, -1],    // left
        ]
    }

    start() {

    }

    set_direction(d) {
        this.direction = d;
    }

    check_tail_increasing() {
        // check current length of snake
        if (this.step <= 10) return true; // first 10 steps must increase
        // then every 3 steps increase one
        if (this.step % 3 === 1) return true;
        return false;
    }

    next_step() { 
        // change snake to next step
        const d = this.direction; // get current direction
        // position
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        //this.eye_direciton = d; // eye direction is same as movement
        this.direction = -1;    // clear direction
        this.status = "move";   // change to move
        this.step++; // go to next step

        const k = this.cells.length;
        // move forward each cell 
        for (let i = k; i > 0; i--) {
            // this.cells[i]=this.cells[i-1] but has referrence issue
            // deep copy
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
        // if touch blocks, die
        // if (!this.gamemap.check_valid(this.next_cell)) {
        //     this.status = "die";
        // }
    }

    update_move() {
        console.log("move");
        // find dx and dy
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        // calculate distance
        const distance = Math.sqrt(dx * dx + dy * dy); // Manhattan distance

        // check if reached target position
        if (distance < this.eps) {
            this.cells[0] = this.next_cell;     // replace head with next
            this.next_cell = null;              // 
            this.status = "idle";               // set to idle status

            // if tail is not increasing, remove tail
            if (!this.check_tail_increasing()) {
                this.cells.pop();               
            }

        } else {
            const move_distance = this.speed * this.timedelta / 1000; // distance between two frames
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1];
                const tail_target = this.cells[k - 2];

                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    update() {
        if (this.status === 'move') {
            this.update_move();
        }
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        if (this.status === "die") {
            ctx.fillStyle = "white";
        }

        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L * 0.8 / 2, 0, Math.PI * 2);
            ctx.fill();
        }

        // enumerate two neighbor cells of the same snake
        for (let i = 1; i < this.cells.length; i++) {
            // first is a, second is b
            const a = this.cells[i - 1];
            const b = this.cells[i];
            // if the two parts are already the same
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            // vertical
            if (Math.abs(a.x - b.x) < this.eps) {
                // left-upper corner coord , width, height
                // *L to convert to absolute distance
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            }
            // horizontal  
            else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);

            }
        }

        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i++) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;

            ctx.beginPath(); // draw circle
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }
}