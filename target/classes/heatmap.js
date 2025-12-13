import { initializeApp } from "https://www.gstatic.com/firebasejs/11.0.0/firebase-app.js";
import { getDatabase, ref, onValue } from "https://www.gstatic.com/firebasejs/11.0.0/firebase-database.js";

// ---- Your Firebase config (same one you used for the form) ----
const firebaseConfig = {
    apiKey: "AIzaSyBIk4vUtLBoYMy45i-72RziW0SMOFJRXzo",
    authDomain: "csclab7325.firebaseapp.com",
    databaseURL: "https://csclab7325-default-rtdb.firebaseio.com",
    projectId: "csclab7325",
    storageBucket: "csclab7325.firebasestorage.app",
    messagingSenderId: "211533473195",
    appId: "1:211533473195:web:1308bb15313568045b5eee",
    measurementId: "G-F8PZYRY5WW"
};

const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

// Path where your form writes:
const responsesRef = ref(db, "heatmapResponses");

// ---- Your normalized coordinates ----
const AREA_POINTS = {
    "Nold Hall": { x: 0.155, y: 0.585 },
    "The Great Lawn": { x: 0.365, y: 0.600 },
    "Lupton Hall": { x: 0.335, y: 0.470 },
    "Roosevelt Hall": { x: 0.410, y: 0.585 },

    "Hale Hall": { x: 0.510, y: 0.395 },
    "Greenley Hall": { x: 0.515, y: 0.485 },
    "Knapp Hall": { x: 0.530, y: 0.565 },
    "Business School": { x: 0.540, y: 0.645 },
    "Memorial Hall": { x: 0.460, y: 0.695 },
    "Health": { x: 0.490, y: 0.735 },

    "Gleeson Hall": { x: 0.615, y: 0.415 },
    "Ward Hall": { x: 0.580, y: 0.455 },
    "Whitman Hall": { x: 0.600, y: 0.510 },
    "Thompson Hall": { x: 0.640, y: 0.505 },
    "Hicks Hall": { x: 0.670, y: 0.525 },
    "Little Theater": { x: 0.675, y: 0.565 },

    "Campus Center": { x: 0.650, y: 0.345 },
    "Conklin Hall": { x: 0.600, y: 0.355 },
    "Auditorium": { x: 0.670, y: 0.395 },
    "Horton Hall": { x: 0.735, y: 0.350 },
    "Police": { x: 0.780, y: 0.375 },

    "Teaching Gardens": { x: 0.730, y: 0.500 },
    "Greenhouse": { x: 0.770, y: 0.545 },
    "Biotech Facility": { x: 0.880, y: 0.375 },

    "Sinclair Hall": { x: 0.600, y: 0.755 },
    "Dewey Hall": { x: 0.570, y: 0.785 },
    "Alumni Hall": { x: 0.515, y: 0.755 },
    "Applied Social Science": { x: 0.660, y: 0.665 },
    "Building Systems": { x: 0.750, y: 0.675 },
    "Children's Center": { x: 0.820, y: 0.785 },
    "Orchard Hall": { x: 0.920, y: 0.855 }
};

// Turn text activity levels into scores
const ACTIVITY_SCORES = {
    Low: 1,
    Moderate: 2,
    High: 3
};

const mapImg = document.getElementById("campus-map");
const overlay = document.getElementById("heat-overlay");

// Re-render heatmap whenever RTDB data changes
onValue(responsesRef, snapshot => {
    const totals = {};
    const counts = {};

    snapshot.forEach(child => {
        const val = child.val();
        const area = val.area;
        const level = val.activityLevel;

        if (!AREA_POINTS[area]) return;

        const score = ACTIVITY_SCORES[level] ?? 1;
        totals[area] = (totals[area] || 0) + score;
        counts[area] = (counts[area] || 0) + 1;
    });

    const averages = {};
    Object.keys(totals).forEach(area => {
        averages[area] = totals[area] / counts[area]; // 1–3
    });

    renderHeatmap(averages);
});

// Draw dots on the overlay
function renderHeatmap(areaScores) {
    overlay.innerHTML = "";

    const width = mapImg.clientWidth;
    const height = mapImg.clientHeight;

    Object.entries(areaScores).forEach(([area, avgScore]) => {
        const pt = AREA_POINTS[area];
        if (!pt) return;

        const dot = document.createElement("div");
        dot.className = "heat-dot";
        dot.dataset.label = `${area} (${avgScore.toFixed(1)})`;

        // convert normalized coords → pixels
        const x = pt.x * width;
        const y = pt.y * height;
        dot.style.left = `${x}px`;
        dot.style.top = `${y}px`;

        // normalize intensity between 0 and 1 (1–3 → ~0.33–1)
        const intensity = Math.min(avgScore / 3, 1);
        dot.style.setProperty("--intensity", intensity.toString());

        overlay.appendChild(dot);
    });
}