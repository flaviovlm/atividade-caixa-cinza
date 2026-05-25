import { useState, useEffect } from "react";
import "./Login.css";

export default function Login({ onSwitchToRegister }) {
  const [form, setForm] = useState({ email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [successMsg, setSuccessMsg] = useState("");
  
  // Controle de Tema Sincronizado
  const [theme, setTheme] = useState(localStorage.getItem("theme") || "dark");

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
    localStorage.setItem("theme", theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((prev) => (prev === "dark" ? "light" : "dark"));
  };

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError("");
    setSuccessMsg("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setSuccessMsg("");

    try {
      const res = await fetch("http://localhost:8080/user/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      const data = await res.json();

      if (res.ok && data.success) {
        setSuccessMsg(data.message || "Login efetuado com sucesso!");
        // Redirecione o usuário aqui se necessário
      } else {
        // Exibe erro genérico e seguro de validação
        setError(data.message || "E-mail ou senha inválidos.");
      }
    } catch {
      setError("Não foi possível conectar ao servidor.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      {/* Botão de Tema Clean (Sem Emojis) */}
      <button type="button" className="theme-toggle-btn" onClick={toggleTheme}>
        {theme === "dark" ? "Modo Claro" : "Modo Escuro"}
      </button>

      <div className="auth-ornament-tl" />
      <div className="auth-ornament-br" />

      <div className="auth-card">
        <div className="auth-card-header">
          <a href="/" className="auth-logo">FVL</a>
          <p className="auth-eyebrow">Área de Acesso</p>
          <h1 className="auth-title">Entrar</h1>
          <div className="auth-title-line" />
        </div>

        <form className="auth-form" onSubmit={handleSubmit} noValidate>
          <div className="form-group">
            <label htmlFor="email">E-mail</label>
            <input
              id="email"
              name="email"
              type="email"
              placeholder="seu@email.com"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Senha</label>
            <input
              id="password"
              name="password"
              type="password"
              placeholder="••••••••••"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>

          {error && <p className="auth-feedback auth-error">{error}</p>}
          {successMsg && <p className="auth-feedback auth-success">{successMsg}</p>}

          <button type="submit" className="auth-btn" disabled={loading}>
            {loading ? <span className="auth-spinner" /> : "Acessar"}
          </button>
        </form>

        <p className="auth-switch">
          Não tem uma conta?{" "}
          <button className="auth-switch-btn" onClick={onSwitchToRegister}>
            Cadastre-se
          </button>
        </p>
      </div>
    </div>
  );
}