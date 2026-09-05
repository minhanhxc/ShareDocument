<template>
  <footer class="app-footer">
    <div class="footer-container">
      <!-- Brand & Copyright -->
      <div class="footer-brand-col">
        <span class="footer-brand-title">DocShare</span>
        <span class="footer-copy">© 2026 DocShare.</span>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.app-footer {
  background-color: #f2f3ff;
  border-top: 1px solid #c3c6d7;
  margin-top: auto;
  width: 100%;
}

.footer-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 32px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

@media (min-width: 768px) {
  .footer-container {
    flex-direction: row;
  }
}

.footer-brand-col {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

@media (min-width: 768px) {
  .footer-brand-col {
    align-items: flex-start;
  }
}

.footer-brand-title {
  font-size: 18px;
  font-weight: 700;
  color: #131b2e;
}

.footer-copy {
  font-size: 13px;
  color: #434655;
}

.footer-nav {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 16px 24px;
}

.footer-link {
  font-size: 13px;
  color: #434655;
  text-decoration: underline;
  text-underline-offset: 3px;
  transition: color 0.2s ease;
}

.footer-link:hover {
  color: #004ac6;
}
</style>
