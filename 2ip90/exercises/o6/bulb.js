const AMOUNT_OF_BULBS = 50;
let bulbs = Array(AMOUNT_OF_BULBS).fill(true);
let i = 0;

const iterate = () => {
    if(!bulbs[i % AMOUNT_OF_BULBS]) {
        return;
    }

    const nextI = (i+1) % AMOUNT_OF_BULBS;
    bulbs[nextI] = !bulbs[nextI];
}

iterate();

for(i = 1; bulbs.includes(false); i++) {
    iterate();
}

console.log(i);
