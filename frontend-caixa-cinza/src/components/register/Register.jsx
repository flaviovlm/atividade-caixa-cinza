import { useState, useEffect } from "react";
import "./Register.css"


const ROLES = [
  { value: "ADMIN", label: "Administrador" },
  { value: "GERENTE", label: "Gerente" },
  { value: "CLIENTE", label: "Cliente" },
];

export default function Register({ onSwitchToLogin }) {
  const [form, setForm] = useState({ name: "", email: "", password: "", role: "" });
  const [loading, setLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");
  const [errors, setErrors] = useState({});
  const [apiError, setApiError] = useState("");
  
  // Controle de Tema Global (Mantém a escolha após atualizar a página)
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
    setErrors({ ...errors, [e.target.name]: "" });
    setApiError("");
    setSuccessMsg("");
  };

  const passwordCriteria = {
    length: form.password.length >= 10 && form.password.length <= 12,
    hasUpper: /[A-Z]/.test(form.password),
    hasLower: /[a-z]/.test(form.password),
    hasNumber: /[0-9]/.test(form.password),
    hasSpecial: /[!@#$%&*]/.test(form.password),
  };

  const validate = () => {
    const newErrors = {};
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!form.name || form.name.length < 3)
      newErrors.name = "Nome deve ter no mínimo 3 caracteres.";
    if (!form.email || !emailRegex.test(form.email)) 
      newErrors.email = "Informe um e-mail válido.";
    if (Object.values(passwordCriteria).includes(false)) {
      newErrors.password = "A senha ainda não atende a todos os critérios.";
    }
    if (!form.role) newErrors.role = "Selecione um perfil.";
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validation = validate();
    if (Object.keys(validation).length > 0) {
      setErrors(validation);
      return;
    }

    setLoading(true);
    setApiError("");
    setSuccessMsg("");

    try {
      const res = await fetch("http://localhost:8080/user/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      const data = await res.json();

      if (res.ok && data.success) {
        setSuccessMsg("Cadastro realizado com sucesso! Faça login para continuar.");
        setForm({ name: "", email: "", password: "", role: "" });
      } else {
        setApiError(data.message || "Erro ao realizar cadastro.");
      }
    } catch {
      setApiError("Não foi possível conectar ao servidor.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
<button type="button" className="theme-toggle-btn" onClick={toggleTheme}>
  {theme === "dark" ? "Modo Claro" : "Modo Escuro"}
</button>

      <div className="auth-ornament-tl" />
      <div className="auth-ornament-br" />

      <div className="auth-card auth-card--register">
        <div className="auth-card-header">
          <a href="/" className="auth-logo">FVL</a>
          <p className="auth-eyebrow">Área de Acesso</p>
          <h1 className="auth-title">Cadastrar</h1>
          <div className="auth-title-line" />
        </div>

        <form className="auth-form" onSubmit={handleSubmit} noValidate>
          <div className="form-group">
            <label htmlFor="name">Nome</label>
            <input
              id="name"
              name="name"
              type="text"
              placeholder="Seu nome completo"
              value={form.name}
              onChange={handleChange}
              required
            />
            {errors.name && <span className="field-error">{errors.name}</span>}
          </div>

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
            {errors.email && <span className="field-error">{errors.email}</span>}
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
            {errors.password && <span className="field-error">{errors.password}</span>}
            
            {/* Lista Interativa de Validação */}
            <div className="password-checklist">
              <span className={passwordCriteria.length ? "valid" : "invalid"}>
                {passwordCriteria.length ? "✓" : "○"} 10–12 caracteres
              </span>
              <span className={passwordCriteria.hasUpper ? "valid" : "invalid"}>
                {passwordCriteria.hasUpper ? "✓" : "○"} maiúscula
              </span>
              <span className={passwordCriteria.hasLower ? "valid" : "invalid"}>
                {passwordCriteria.hasLower ? "✓" : "○"} minúscula
              </span>
              <span className={passwordCriteria.hasNumber ? "valid" : "invalid"}>
                {passwordCriteria.hasNumber ? "✓" : "○"} número
              </span>
              <span className={passwordCriteria.hasSpecial ? "valid" : "invalid"}>
                {passwordCriteria.hasSpecial ? "✓" : "○"} especial (!@#$%&*)
              </span>
            </div>
          </div>

          <div className="form-group">
            <label>Perfil</label>
            <div className="role-selector">
              {ROLES.map((r) => (
                <label key={r.value} className={`role-option ${form.role === r.value ? "selected" : ""}`}>
                  <input
                    type="radio"
                    name="role"
                    value={r.value}
                    checked={form.role === r.value}
                    onChange={handleChange}
                  />
                  {r.label}
                </label>
              ))}
            </div>
            {errors.role && <span className="field-error">{errors.role}</span>}
          </div>

          {apiError && <p className="auth-feedback auth-error">{apiError}</p>}
          {successMsg && <p className="auth-feedback auth-success">{successMsg}</p>}

          <button type="submit" className="auth-btn" disabled={loading}>
            {loading ? <span className="auth-spinner" /> : "Cadastrar"}
          </button>
        </form>

        <p className="auth-switch">
          Já tem conta?{" "}
          <button className="auth-switch-btn" onClick={onSwitchToLogin}>
            Entrar
          </button>
        </p>
      </div>
    </div>
  );
}