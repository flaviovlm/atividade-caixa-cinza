import { useState } from "react";
import Login from "./components/login/Login";
import Register from "./components/register/Register";

export default function App() {
  const [view, setView] = useState("login"); // "login" | "register"

  return view === "login"
    ? <Login onSwitchToRegister={() => setView("register")} />
    : <Register onSwitchToLogin={() => setView("login")} />;
}
