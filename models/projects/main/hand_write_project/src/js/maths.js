var answer;
var score = 0;
var backgroundImages = [];

function nextQuestion() {
  const n1 = Math.floor(Math.random() * 5);
  document.getElementById("n1").innerHTML = n1;

  const n2 = Math.floor(Math.random() * 6);
  document.getElementById("n2").innerHTML = n2;
  answer = n1 + n2;
}

function checkAnswer() {
  const prediction = predictImage();
  console.log(`Answer: ${answer}, prediction: ${prediction}`);

  if (prediction == answer) {
    score++;
    console.log(`Correct! Score: ${score}`);
    if (score <= 6) {
      backgroundImages.push(`url('images/background${score}.svg')`);
      document.body.style.backgroundImage = backgroundImages;
    } else {
      alert(
        "Well done! Your maths garden is in full bloom! Want to start again?"
      );
      score = 0;
      backgroundImages = [];
      document.body.style.backgroundImage = backgroundImages;
    }
  } else {
    if (score != 0) {
      score--;
    }
    console.log(`Wrong. Score ${score}`);

    alert(
      "Oops! Check your calculations and try writing the number neater next time!"
    );
    setTimeout(function () {
      backgroundImages.pop();
      document.body.style.backgroundImage = backgroundImages;
    }, 1000);
  }
}
