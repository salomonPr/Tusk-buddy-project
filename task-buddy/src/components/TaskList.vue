<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getTasks, createTask, updateTask, deleteTask, clearToken } from '../services/api'

const router = useRouter()
const currentUser = ref(null)
const tasks = ref([])
const activeView = ref('all')
const showModal = ref(false)
const error = ref('')

const newTask = ref({ title: '', description: '', priority: 'MEDIUM', status: 'TODO' })

onMounted(async () => {
  const user = JSON.parse(localStorage.getItem('currentUser') || 'null')
  if (!user) { router.push('/login'); return }
  currentUser.value = user
  try {
    const res = await getTasks()
    tasks.value = res.data
  } catch {
    router.push('/login')
  }
})

const filteredTasks = computed(() => {
  if (activeView.value === 'all') return tasks.value
  if (activeView.value === 'todo') return tasks.value.filter(t => t.status === 'TODO')
  if (activeView.value === 'inprogress') return tasks.value.filter(t => t.status === 'IN_PROGRESS')
  if (activeView.value === 'done') return tasks.value.filter(t => t.status === 'DONE')
  return tasks.value
})

const viewTitle = computed(() => {
  if (activeView.value === 'all') return 'All Tasks'
  if (activeView.value === 'todo') return 'To Do'
  if (activeView.value === 'inprogress') return 'In Progress Tasks'
  if (activeView.value === 'done') return 'Completed Tasks'
  return 'Tasks'
})

const viewSubtitle = computed(() => `Here's a list of your tasks for this week.`)

const userInitial = computed(() => currentUser.value?.username?.charAt(0).toUpperCase() || '?')

const priorityClass = (priority) => {
  if (priority === 'HIGH') return 'badge-high'
  if (priority === 'MEDIUM') return 'badge-medium'
  return 'badge-low'
}

const handleStatusChange = async (task, newStatus) => {
  try {
    const res = await updateTask(task.id, { ...task, status: newStatus })
    const i = tasks.value.findIndex(t => t.id === task.id)
    tasks.value[i] = res.data
  } catch {
    error.value = 'Failed to update task.'
  }
}

const handleDeleteTask = async (id) => {
  try {
    await deleteTask(id)
    tasks.value = tasks.value.filter(t => t.id !== id)
  } catch {
    error.value = 'Failed to delete task.'
  }
}

const openModal = () => {
  newTask.value = { title: '', description: '', priority: 'MEDIUM', status: 'TODO' }
  showModal.value = true
}

const handleCreate = async () => {
  if (!newTask.value.title.trim()) return
  try {
    const res = await createTask({ ...newTask.value })
    tasks.value.push(res.data)
    showModal.value = false
  } catch {
    error.value = 'Failed to create task.'
  }
}

const logout = () => {
  clearToken()
  localStorage.removeItem('currentUser')
  router.push('/login')
}
</script>

<template>
  <div class="dashboard">

    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-logo">Task-Buddy</div>

      <nav class="sidebar-nav">
        <button :class="['nav-item', activeView === 'all' && 'active']" @click="activeView = 'all'">Dashboard</button>
        <button :class="['nav-item', activeView === 'todo' && 'active']" @click="activeView = 'todo'">To Do</button>
        <button :class="['nav-item', activeView === 'inprogress' && 'active']" @click="activeView = 'inprogress'">In Progress</button>
        <button :class="['nav-item', activeView === 'done' && 'active']" @click="activeView = 'done'">Completed</button>
      </nav>

      <div class="sidebar-divider"></div>

      <div class="sidebar-account">
        <p class="account-label">ACCOUNT</p>
        <div class="account-user">
          <div class="avatar">{{ userInitial }}</div>
          <span class="username">{{ currentUser?.username }}</span>
        </div>
        <button class="logout-btn" @click="logout">Log out</button>
      </div>
    </aside>

    <!-- Main content -->
    <main class="main">
      <div class="main-header">
        <div>
          <h1 class="main-title">{{ viewTitle }}</h1>
          <p class="main-subtitle">{{ viewSubtitle }}</p>
        </div>
        <button class="create-btn" @click="openModal">+ Create Task</button>
      </div>

      <p v-if="error" class="error-msg">{{ error }}</p>

      <div class="cards-grid">
        <div
          v-for="task in filteredTasks"
          :key="task.id"
          class="task-card"
          :class="{ 'card-done': task.status === 'DONE' }"
        >
          <span v-if="task.priority" :class="['priority-badge', priorityClass(task.priority)]">
            {{ task.priority }}
          </span>

          <h3 class="card-title">{{ task.title }}</h3>
          <p v-if="task.description" class="card-desc">{{ task.description }}</p>

          <div class="card-footer">
            <select
              class="status-select"
              :value="task.status"
              @change="handleStatusChange(task, $event.target.value)"
            >
              <option value="TODO">To Do</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="DONE">Done</option>
            </select>
            <button class="delete-btn" @click="handleDeleteTask(task.id)">✕</button>
          </div>
        </div>

        <p v-if="filteredTasks.length === 0" class="empty-hint">No tasks here. Click "+ Create Task" to add one.</p>
      </div>
    </main>

    <!-- Create Task Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
      <div class="modal">
        <h2 class="modal-title">Create Task</h2>

        <label class="modal-label">Title</label>
        <input v-model="newTask.title" class="modal-input" placeholder="Task title" />

        <label class="modal-label">Description</label>
        <textarea v-model="newTask.description" class="modal-input modal-textarea" placeholder="Optional description"></textarea>

        <label class="modal-label">Priority</label>
        <select v-model="newTask.priority" class="modal-input">
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
        </select>

        <label class="modal-label">Status</label>
        <select v-model="newTask.status" class="modal-input">
          <option value="TODO">To Do</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="DONE">Done</option>
        </select>

        <div class="modal-actions">
          <button class="modal-cancel" @click="showModal = false">Cancel</button>
          <button class="modal-submit" @click="handleCreate">Create</button>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>
.dashboard {
  display: flex;
  min-height: 100vh;
  background: #fff;
  font-family: Inter, sans-serif;
}

/* ── Sidebar ── */
.sidebar {
  width: 260px;
  min-width: 260px;
  border-right: 1px solid #e5e7eb;
  display: flex;
  flex-direction: column;
  padding: 28px 20px;
  background: #fff;
}

.sidebar-logo {
  font-size: 1.2rem;
  font-weight: 700;
  color: #1a202c;
  margin-bottom: 32px;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.nav-item {
  text-align: left;
  background: none;
  border: none;
  padding: 10px 14px;
  border-radius: 6px;
  font-size: 0.95rem;
  color: #374151;
  cursor: pointer;
  transition: background 0.15s;
}

.nav-item:hover { background: #f3f4f6; }
.nav-item.active { background: #f3f4f6; font-weight: 600; }

.sidebar-divider {
  border-top: 1px solid #e5e7eb;
  margin: 24px 0;
}

.account-label {
  font-size: 0.7rem;
  font-weight: 600;
  color: #9ca3af;
  letter-spacing: 0.08em;
  margin-bottom: 12px;
}

.account-user {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #1a202c;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 0.95rem;
}

.username {
  font-size: 0.9rem;
  font-weight: 500;
  color: #1a202c;
}

.logout-btn {
  background: none;
  border: none;
  color: #e53e3e;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  padding: 0;
}

.logout-btn:hover { text-decoration: underline; }

/* ── Main ── */
.main {
  flex: 1;
  padding: 36px 40px;
  background: #fff;
}

.main-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 32px;
}

.main-title {
  font-size: 2rem;
  font-weight: 800;
  color: #1a202c;
  margin: 0 0 4px 0;
}

.main-subtitle {
  font-size: 0.9rem;
  color: #6b7280;
  margin: 0;
}

.create-btn {
  background: #1a202c;
  color: #fff;
  border: none;
  padding: 12px 22px;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
}

.create-btn:hover { background: #2d3748; }

.error-msg { color: #e53e3e; margin-bottom: 16px; font-size: 0.875rem; }

/* ── Cards ── */
.cards-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.task-card {
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 20px;
  width: 320px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  transition: box-shadow 0.15s;
}

.task-card:hover { box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
.card-done { opacity: 0.6; }

.priority-badge {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 0.72rem;
  font-weight: 700;
  letter-spacing: 0.05em;
  width: fit-content;
}

.badge-high   { background: #fee2e2; color: #dc2626; }
.badge-medium { background: #fef3c7; color: #d97706; }
.badge-low    { background: #dcfce7; color: #16a34a; }

.card-title {
  font-size: 1.05rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0;
}

.card-desc {
  font-size: 0.875rem;
  color: #6b7280;
  margin: 0;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 8px;
}

.status-select {
  border: 1px solid #d1d5db;
  border-radius: 6px;
  padding: 5px 10px;
  font-size: 0.85rem;
  color: #374151;
  background: #fff;
  cursor: pointer;
}

.delete-btn {
  background: none;
  border: none;
  color: #9ca3af;
  font-size: 0.9rem;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.delete-btn:hover { background: #fee2e2; color: #dc2626; }

.empty-hint {
  color: #9ca3af;
  font-size: 0.9rem;
  margin-top: 40px;
  width: 100%;
  text-align: center;
}

/* ── Modal ── */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.35);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.modal {
  background: #fff;
  border-radius: 12px;
  padding: 32px;
  width: 440px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.15);
}

.modal-title {
  font-size: 1.25rem;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 8px 0;
}

.modal-label {
  font-size: 0.8rem;
  font-weight: 600;
  color: #374151;
  margin-top: 4px;
}

.modal-input {
  width: 100%;
  padding: 9px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 0.9rem;
  color: #1a202c;
  background: #fff;
  box-sizing: border-box;
}

.modal-input:focus { outline: none; border-color: #1a202c; }

.modal-textarea { resize: vertical; min-height: 80px; }

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 8px;
}

.modal-cancel {
  background: #f3f4f6;
  border: none;
  padding: 9px 20px;
  border-radius: 6px;
  font-size: 0.9rem;
  cursor: pointer;
  color: #374151;
}

.modal-submit {
  background: #1a202c;
  color: #fff;
  border: none;
  padding: 9px 20px;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
}

.modal-submit:hover { background: #2d3748; }
</style>
