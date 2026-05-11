const { initializeApp } = require('firebase/app');
const { getAuth, signInWithEmailAndPassword } = require('firebase/auth');

const firebaseConfig = {
  apiKey: "",
  authDomain: "directoryapplication-74e23.firebaseapp.com",
  projectId: "directoryapplication-74e23",
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

async function login() {
  try {
    const userCredential = await signInWithEmailAndPassword(auth, "test@test.com", "123456");
    const idToken = await userCredential.user.getIdToken(true); // true = force refresh

    console.log("\n✅ УСПЕШНО ЗАЛОГИНИЛИСЬ!");
    console.log("Email:", userCredential.user.email);
    console.log("\n🔑 ID Token (скопируй это):");
    console.log(idToken);

  } catch (error) {
    console.error("❌ Ошибка входа:", error.message);
  }
}

login();