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
        if (!g[x][y] && this.check_connectivity(g, x, y, tx, ty)) {
            return true;
        }
    }
    // cannot find a path
    return false;
}