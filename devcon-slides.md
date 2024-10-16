---
theme: gaia
marp: true
class: invert
paginate: true
---
<!-- _class: lead -->

# Liferay Workspace Testing

---

# So What about Testing

- We all know testing is important
- We all know automated functional end-to-end testing in CI is what we really need
- But we all know this is really hard

---

# Why is it hard

- manual steps must be automated and reproducible in heteogeneous environments
- automating manual steps can be complex
-- don't know what tools you can use to make this doable (gradle or npm tasks)
- containization and orchestration is not trivial
-- startup external process, wait for readiness, etc (health checks)
- You don't know when you are done
-- Have I covered enough of my code?
-- Have I validated it will work in production environment?

---

